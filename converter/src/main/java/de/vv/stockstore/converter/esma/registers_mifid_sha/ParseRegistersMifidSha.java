package de.vv.stockstore.converter.esma.registers_mifid_sha;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ParseRegistersMifidSha {

	public static Object loadConfig(String file, Object o) {
		Gson gson = new Gson();
		try {
			o = gson.fromJson(new FileReader(file), o.getClass());
			System.out.println("ok");
			return o;
		} catch (JsonSyntaxException e) {
			System.err.println("JsonSyntaxException: " +e.getMessage());
		} catch (JsonIOException e) {
			System.err.println("JsonIOException: " +e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " +e.getMessage());
		}
		System.out.println("Error");
		return null;
	}
	
}
