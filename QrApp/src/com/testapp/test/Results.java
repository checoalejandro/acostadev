package com.testapp.test;

import helper.json.UserAnswer;

import java.util.ArrayList;

public class Results {
	public static ArrayList<UserAnswer> useranswers = new ArrayList<UserAnswer>();
	public static int corrects = 0;
	public static int incorrects = 0;
	public static int total = 0;
	
	public static ArrayList<UserAnswer> getAnswers(){
		return Results.useranswers;
	}
	
	public static void clearResults(){
		useranswers.clear();
		corrects = 0;
		incorrects = 0;
		total = 0;
	}
	
}
