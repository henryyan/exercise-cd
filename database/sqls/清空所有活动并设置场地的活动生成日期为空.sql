delete from t_field_badmintoon_activity;
delete from t_field_badmintoon_activity_tactics;
delete from t_field_football_activity;
delete from t_field_football_activity_tactics;
delete from t_field_tennis_activity;
delete from t_field_tennis_activity_tactics;

delete from t_field_order;

update t_field_badmintoon set issue_last_date = null;
update t_field_football set issue_last_date = null;
update t_field_tennis set issue_last_date = null;