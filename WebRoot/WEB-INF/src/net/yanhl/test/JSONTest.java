package net.yanhl.test;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

public class JSONTest {
	public static void main(String[] args) {
		String personData = "{\"birthday\":\"1980/01/01\",\"name\":\"testname\"}";   
        JSONObject jsonPerson = JSONObject.fromObject(personData);    
        String[] dateFormats = new String[] {"yyyy/MM/dd"};    
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));    
        Person person = (Person)JSONObject.toBean(jsonPerson, Person.class);   
        System.out.println(person.getBirthday());
	}
}
