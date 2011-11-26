package test;

public class CreateDbunit {
	public static void main(String[] args) {
		String tables = "t_account_order,t_admin_authority,t_admin_role,t_admin_role_authority,t_admin_sms,t_admin_system_property,t_admin_user,t_admin_user_role,t_business_info,t_card_discount,t_card_order,t_card_type,t_card_usage_record,t_field_badmintoon,t_field_badmintoon_activity,t_field_badmintoon_activity_tactics,t_field_badmintoon_basic_price,t_field_badmintoon_weekend_price,t_field_football,t_field_football_activity,t_field_football_activity_tactics,t_field_football_basic_price,t_field_football_weekend_price,t_field_order,t_field_tennis,t_field_tennis_activity,t_field_tennis_activity_tactics,t_field_tennis_basic_price,t_field_tennis_weekend_price,t_field_type,t_information,t_link_venue_fieldtype,t_member_card,t_member_user,t_sys_user,t_tactics,t_tactics_date,t_tactics_price,t_v_all_activity,t_venue_financial_statement,t_venue_info,t_venue_member_info,t_venue_member_info_copy,t_venue_user";
		args = tables.split(",");
		for (int i = 0; i < args.length; i++) {
			System.out.println("<table name=\"" + args[i] + "\" />");
		}
	}
}
