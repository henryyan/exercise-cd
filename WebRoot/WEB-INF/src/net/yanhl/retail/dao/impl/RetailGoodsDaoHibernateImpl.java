package net.yanhl.retail.dao.impl;

import org.springframework.stereotype.Repository;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.retail.dao.RetailGoodsDao;

@SuppressWarnings("unchecked")
@Repository(value = "retailGoodsDao")
public class RetailGoodsDaoHibernateImpl extends BaseDaoHibernate implements RetailGoodsDao {

}
