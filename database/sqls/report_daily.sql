CREATE PROCEDURE `report_daily`(
        $venue_id INT,
        $diff_date VARCHAR(250),
        $activity_status VARCHAR(250)
    )
    NOT DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    character set utf8
    COMMENT ''
BEGIN
	DECLARE fetchSeqOk BOOLEAN; ## define the flag for loop judgement
	DECLARE _field_name VARCHAR(50);
	DECLARE _field_id BIGINT;
	DECLARE _cnt INT;
	DECLARE _count INT;	
	DECLARE _sumprice INT;
	DECLARE _str VARCHAR(1000);
	DECLARE sqlstr VARCHAR(1000);
	DECLARE _sql VARCHAR(1000);
	DECLARE fetchSeqCursor CURSOR FOR 
		SELECT DISTINCT FIELD_NAME, FIELD_ID 
			FROM t_field_badmintoon_activity 
				WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date
				ORDER BY FIELD_ID;   
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET fetchSeqOk = TRUE;
	
	SELECT COUNT(DISTINCT FIELD_ID) FROM t_field_badmintoon_activity 
				WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date
	INTO _cnt;
	SET $activity_status ='锻炼';	
	SET fetchSeqOk = FALSE; 	
	SET _count = 0;
	SET _str = "";
	
	SET @table_name = CONCAT('tmp_venueID',$venue_id,'_',YEAR($diff_date),'_',MONTH($diff_date),'_',DAY($diff_date));
	SET @drop_table = CONCAT('drop table if exists ',@table_name);
	PREPARE s1 FROM @drop_table;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
	
	-- Check whether the table exists or not
	SET @create_table = CONCAT('create table if not exists ',
								@table_name,
								' (ID	bigint auto_increment not null,	period varchar(250), primary key (ID));');
	PREPARE s1 FROM @create_table;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
	SET _sql =' ';
	SET @COLUMNS='';
	OPEN fetchSeqCursor;
		WHILE NOT fetchSeqOk DO
		-- BUILD TEMP TABLE add columns 
			
				FETCH fetchSeqCursor INTO _field_name, _field_id;				
				IF _cnt > 0 THEN
					SET @COLUMNS = CONCAT(@COLUMNS,' add `',_field_id,'` varchar(100) not null, ');
					SET _sql = CONCAT(_sql,',`',_field_id,'` ',_field_name);
					-- count 
					SELECT COUNT(*) FROM t_field_badmintoon_activity 
						WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date AND FIELD_ID LIKE _field_id AND ACTIVITY LIKE $activity_status
					INTO _count;
					SET _str = CONCAT(_str,',"',_count,'"');
					SET _count = 0;
				END IF;
				SET _cnt = _cnt -1;
			
		END WHILE;
		SET @COLUMNS= CONCAT(@COLUMNS,' add `status_total` int, add `sum_price` int, add `from_time` time; ');
		
		SET @stmt1 = CONCAT('alter table ',@table_name,@COLUMNS);
		-- select @stmt1;
			
		PREPARE s1 FROM @stmt1;
		EXECUTE s1;
		DEALLOCATE PREPARE s1; 
	
		-- insert total record
		SET _str = CONCAT(_str,',',0);
		SET _str = CONCAT(_str,',',0);
						
		SET @stmt1 = CONCAT('insert into ',@table_name,' values ("","',$activity_status,'_total"',_str,',"00-00-00")');
		
		-- select @stmt1;
			PREPARE s1 FROM @stmt1;
			EXECUTE s1;
			DEALLOCATE PREPARE s1;
	CLOSE fetchSeqCursor; 
	
	BEGIN -- 填充数据
		DECLARE fetchSeqOk2 BOOLEAN;
		-- declare _cnt int;
		DECLARE _period  VARCHAR(50);
		DECLARE _field_name  VARCHAR(250);
		DECLARE _field_id  BIGINT;
		DECLARE t_period  VARCHAR(50);
		DECLARE	_activity  VARCHAR(50);
		DECLARE str  VARCHAR(1000);
		DECLARE _from_time  TIME;
		DECLARE _price INT;
		DECLARE _payment_sum INT;
		DECLARE count_status INT;
		DECLARE total_price INT;
		DECLARE total_count INT;
		DECLARE t_time TIME;
		DECLARE fetchSeqCursor2 CURSOR FOR 
			SELECT FIELD_NAME, a.FIELD_ID, PERIOD, ACTIVITY, PRICE, PAYMENT_SUM, FROM_TIME
				FROM t_field_badmintoon_activity a
					LEFT JOIN t_field_order ON order_id = t_field_order.id
					WHERE 1  AND a.VENUE_ID = $venue_id AND USABLE_DATE = $diff_date					
					ORDER BY PERIOD,FIELD_id; 
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET fetchSeqOk2 = TRUE;
		
		SET fetchSeqOk2 = FALSE;
		
		SELECT DISTINCT COUNT(FIELD_ID)
			FROM t_field_badmintoon_activity 
				WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date
		INTO _cnt; 
		
		-- init params
		SET t_period = '';
		SET t_time ='';
		SET str = '';
		SET _sumprice=0;
		SET count_status = 0;
		SET total_price =0;
		SET total_count =0;
		
		OPEN fetchSeqCursor2;
			WHILE NOT fetchSeqOk2 DO
			-- get item
				FETCH fetchSeqCursor2 INTO _field_name, _field_id, _period, _activity, _price,_payment_sum, _from_time;
				SET _cnt = _cnt -1;
				SET _price = IFNULL(_payment_sum,_price);
				IF t_period = '' AND NOT(_period IS NULL) THEN
					SET t_period = _period;
					SET t_time = _from_time;
				END IF;
				
				IF t_period <> _period OR _cnt <0 THEN
					SET str = CONCAT(str,',',count_status);
					SET str = CONCAT(str,',',_sumprice);
					
					SET @stmt2 = CONCAT('insert into ',@table_name,' values ("","',t_period,'"',str,',"',t_time,'"',')');
					
					SET total_price = total_price + _sumprice;
					SET total_count = total_count + count_status;
					SET str = '';
					SET count_status = 0;
					SET _sumprice = 0;
					
					PREPARE s2 FROM @stmt2;
					EXECUTE s2;
					DEALLOCATE PREPARE s2;
					
					SET t_period = _period;
					SET t_time = _from_time;
				END IF;					
					
				SET str = CONCAT(str,',"',CONVERT(_activity USING utf8),',',_price,'"');
				
				IF _activity  = $activity_status THEN
					-- 计算当前传入状态
					SET count_status = count_status + 1; 
					SET _sumprice = _sumprice + _price;
				END IF;
							
			END WHILE;
					
		CLOSE fetchSeqCursor2;
		-- update total
		SET @ss = CONCAT('update ',@table_name,' set status_total=',total_count,', sum_price=',total_price,' where id =1');
		PREPARE s1 FROM @ss;
		EXECUTE s1;
		DEALLOCATE PREPARE s1;
	
	END;
	
	SET @ss = CONCAT('select id,period',_sql,',status_total,sum_price from ',@table_name," order by from_time");
	PREPARE s1 FROM @ss;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
	
	PREPARE s1 FROM @drop_table;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
END;