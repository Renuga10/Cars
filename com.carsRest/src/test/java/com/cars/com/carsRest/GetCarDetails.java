package com.cars.com.carsRest;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParser;

import io.restassured.path.json.JsonPath;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class GetCarDetails {
	
	
	
	
	/*Extract Json object as string and return as jsonstring*/
	
	public String returnResponseString() throws IOException, ParseException{
		JSONParser parser = new JSONParser();
        //Use JSONObject for simple JSON 
        JSONObject data = (JSONObject) parser.parse(new FileReader("C:/rightcomply/Data/workspace/com.carsRest/src/test/java/com/cars/com/carsRest/cars2.json"));
		String res=data.toJSONString();
	
		return res;
	}
	public List<Integer> discountPrice(JsonPath jsRes){
		/*get list of price and store it in listOfPrice*/
		List<Integer> listOfPrice=jsRes.get("Cars.perdayrent.price");
		List<Integer> listOfDiscount=jsRes.get("Cars.perdayrent.discount");
		
		/*create array list to store price with discount*/
		
		List<Integer> listOfPriceWithDiscount=new ArrayList<Integer>();

		/*find the min value after applying price-(price*discount)/100*/

		for(int i=0; i<listOfPrice.size();i++)
		{
		listOfPriceWithDiscount.add((listOfPrice.get(i)-((listOfPrice.get(i)*listOfDiscount.get(i))/100)));
		
			}
		   
		
		return listOfPriceWithDiscount;
	}

	/*Print all the blue Teslas received in the web service response. Also print the notes*/
	@Test(description="Print All Blue Tesla car with notes")
	public void printBlueTesla() throws IOException, ParseException {
		/*store return response in a string*/			
		String res=returnResponseString();
	/* convert response string to json*/
		JsonPath jsRes=new JsonPath(res);
		
	     int count = jsRes.get("Cars.size()");
	/*Traverse through the loop and check model = tesla and color = blue and print model and notes*/
		for(int i=0; i<count;i++){
		
			
	if((jsRes.get("Cars["+i+"].make").equals("Tesla") ) && (jsRes.get("Cars["+i+"].metadata.color").equals("Blue")))
	{
	System.out.println("Cars...."+i);
	System.out.println("Car Model: "+jsRes.get("Cars["+i+"].make"));
	System.out.println("Car Color: "+jsRes.get("Cars["+i+"].metadata.color"));
	System.out.println("Car Notes: "+jsRes.get("Cars["+i+"].metadata.notes"));
	}
	else
	{
		continue;
	}
					
	
	}
}
	@Test(priority=2,description="Return car details with lowest price per day")
	public void priceOnly() throws IOException, ParseException{
		/*store return response in a string*/			
		String res = returnResponseString();
	/* convert response string to json*/
		JsonPath jsRes=new JsonPath(res);
		jsRes.get("Cars.perdayrent.price.min()");
		/*print car details which has min price*/
		System.out.println("Details of Car whose rent is lowest : "+ jsRes.get("Cars.perdayrent.price.min()"));



}
	
		/*price after discount*/
		@Test(priority=3)
		public void priceAfterDiscount() throws IOException, ParseException{
			/*store return response in a string*/			
			String res = returnResponseString();
			/*call reusable method discountPrice and store it in a list*/
		/* convert response string to json*/
			JsonPath jsRes=new JsonPath(res);
			List<Integer> listOfPriceWithDiscount = discountPrice(jsRes);
			System.out.println("Car details with minumum price");
			int minPriceIndex=listOfPriceWithDiscount.indexOf(Collections.min(listOfPriceWithDiscount));
		
		/*print car details with minimum price with discount*/
		System.out.println(jsRes.get("Cars["+minPriceIndex+"]").toString());

}
		@Test(priority=4,description="Print highest revenue car")
		public void highestRevenueCar() throws IOException, ParseException{
			System.out.println("Print Highest revenue car");
			/*store return response in a string*/			
			String res = returnResponseString();
			/*call reusable method discountPrice and store it in a list*/
			JsonPath jsRes=new JsonPath(res);
			List<Integer>listOfPriceWithDiscount = discountPrice(jsRes);

		
		List<Integer>listOfPrice=jsRes.get("Cars.perdayrent.price");
		/*get list of discount and store it in listOfDiscount*/

		List<Integer>listOfDiscount=jsRes.get("Cars.perdayrent.discount");
		List<Integer> listOfPriceWithDiscount1=new ArrayList();
		/*get list of rental count year to date*/
		List<Integer>listOfRentalCount=jsRes.get("Cars.metrics.rentalcount.yeartodate");
		List<Integer>listOfRevenue=new ArrayList();


		/*add values to listOfPriceWithDiscount after applying price-(price*discount)/100*/

		for(int i=0; i<listOfPrice.size();i++){
		listOfPriceWithDiscount1.add(listOfPrice.get(i)-         
		(listOfPrice.get(i)*listOfDiscount.get(i))/100);
		}
		/*add values to listOfRevenue after applying pricewithdiscount *rentalcount*/
			for(int i=0; i<listOfPrice.size();i++){
				listOfRevenue .add(listOfPriceWithDiscount.get(i)*listOfRentalCount.get(i));
		}
		/*find highest revenue car index*/
		int index = listOfRevenue.indexOf(Collections.max(listOfRevenue));
		/*print highest car revenue*/
		System.out.println(listOfRevenue.toString());
		System.out.println(jsRes.get("Cars["+index+"]"));

}
		@Test(priority=5,description="Print highest profitable car")
		public void highestProfitCar() throws IOException, ParseException{
			/*store return response in a string*/			
			String res = returnResponseString();
		/* convert response string to json*/
			JsonPath jsRes=new JsonPath(res);
		/*get list of price and store it in listOfPrice*/
		List<Integer> listOfPrice=jsRes.get("Cars.perdayrent.price");
		List<Integer> listOfDiscount=jsRes.get("Cars.perdayrent.discount");
		List<Float> listOfMaintanceCost=jsRes.get("Cars.metrics.yoymaintenancecost");
		List<Float> listOfDepreciation=jsRes.get("Cars.metrics.depreciation");
		List<Float> listOfPriceWithDiscount=new ArrayList<Float> ();
		/*get list of rental count year to date*/
		List<Integer> listOfRentalCount=jsRes.get("Cars.metrics.rentalcount.yeartodate");
		List<Float> listOfProfit=new ArrayList<Float> ();


		/*Add values to listOfPriceWithDiscount after applying price-(price*discount)/100*/
		



		for(int i=0; i<listOfPrice.size();i++){
			
			float temp=(listOfPrice.get(i)-((listOfPrice.get(i)*listOfDiscount.get(i))/100));
		    listOfPriceWithDiscount.add((float)temp);
		}
		/*add values to listOfProfit after applying (pricewithdiscount *rentalcount)-(maintanceCost + depreciation)*/
			for(int i=0; i<listOfPrice.size();i++){
				
				
				float temp2=(listOfPriceWithDiscount.get(i)*listOfRentalCount.get(i))-(listOfMaintanceCost.get(i)+listOfDepreciation.get(i));
				
				listOfProfit.add((float)temp2);
		}
		/*find highest revenue car index*/
		int index = listOfProfit.indexOf(Collections.max(listOfProfit));
		/*print highest car revenue*/
		System.out.println(listOfProfit.toString());
		System.out.println("Print Highest Profitable Car details");
		
		System.out.println(jsRes.get("Cars["+index+"]"));

}
}

	



