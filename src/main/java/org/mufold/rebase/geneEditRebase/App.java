package org.mufold.rebase.geneEditRebase;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.*;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Main class
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	App app = new App();
        
    	try{
    		
    		Document document = Jsoup.parse( new File( "E:\\REBASE\\REBASE Enzymes.html" ) , "utf-8" );
    		Elements links = document.select("tr");
    		ArrayList<String> al = new ArrayList<String>();
    		int i=0;
    	    for (Element link : links) 
    	    {
    	    	if(link.attributes().toString().trim().equals("bgcolor=\"#FFFFFF\"")){
    	    		//System.out.println("link : " + link.attributes()); 
    	    		//System.out.println("text : " + link.text());   	    		
    	    		String[] tmpArray = link.text().split("\\s+");
    	    		al.add(tmpArray[0]+";"+tmpArray[1]+";"+app.individualStr(tmpArray[0]));
    	    		i++;
    	    	}
    	    }
    	    FileUtils.writeLines(new File("E:\\REBASE\\rebaseall_putative.txt"), al);
    	      

    	    //Another version
    	    /*
    		//Document document = Jsoup.connect("http://rebase.neb.com/cgi-bin/azlist?re2").get();
    		//Document document = Jsoup.parse( new File( "E:\\REBASE\\REBASE Enzymes.html" ) , "utf-8" );
    		Document document = Jsoup.parse( new File( "E:\\REBASE\\No_Putative\\REBASE Enzymes.html" ) , "utf-8" );
    		Elements links = document.select("a[href]");
    		ArrayList<String> al = new ArrayList<String>();
    		int i=0;
    	    for (Element link : links) 
    	    {
    	    	if(link.attr("target").equals("enz")){
    	    		//System.out.println("link : " + link.attr("href")); 
    	    		//ystem.out.println("text : " + link.text()); 
    	    		al.add(app.individualStr(link.text()));
    	    		i++;
    	    	}
    	    }
    	    
    	    FileUtils.writeLines(new File("E:\\REBASE\\rebaseall.txt"), al);   		
    		*/
    		/*
    		System.out.println(app.individualStr("AaaI"));
    		System.out.println(app.individualStr("AacAA1ORF2169P"));
    		System.out.println(app.individualStr("Aac41ORF1782P"));
    	    System.out.println(app.individualStr("AamI"));
    		System.out.println(app.individualStr("AarI"));
    		System.out.println(app.individualStr("NmeI"));
    		*/	
    	    
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    
    String individualStr(String proteinName){
    	String outStr = "";
    	try{	
    		//Document document = Jsoup.connect("http://rebase.neb.com/rebase/enz/AaaI.html").get();
    		Document document = Jsoup.connect("http://rebase.neb.com/rebase/enz/"+proteinName+".html").get();
    		
    		//System.out.println(document);
    		Elements links = document.select("a[href]");
    		String typeTag = "";
    		String subtypeTag = "";
    		
    		for (Element link : links){
    			if(link.attr("href").indexOf("/rebase/rebtypes.html") == 0){ 
    				//Type II
    	    		//System.out.println(link.text()); 
    	    		typeTag = link.text();
    				
    			}else if(link.attr("href").indexOf("/cgi-bin/sublist") == 0){ 
    				//P
    	    		//System.out.println(link.text().split("subtype: ")[1]); 
    				if( link.text().split("subtype").length>=2){
    					subtypeTag = link.text().split(": ")[1];
    				}else{
    					System.out.println(link.text());
    				}  
    			}
    		}
    		
    		outStr = outStr + typeTag + ";" + subtypeTag + ";";
    		
    		
    		Elements links1 = document.select("font");
    		int i=0;
    		int j=0;
    		boolean sitesTag = false;
    		for (Element link : links1) 
    	    {
    			if(link.attributes().toString().equals(" size=\"2\"")){
    				/*
    				if(i==1 && (Pattern.matches("^[0-9 ATGCN\\^\\(\\)\\/]*$", link.text()))){
    					//System.out.println(i+"\t"+link.attributes());
    					//GTY^RAC
    		    		//System.out.println(link.text());
    		    		outStr = outStr + link.text() + ";";
    					
    				}
    				else if(i==1 ){
    					outStr = outStr + ";";
    				}
    				*/
    				if(link.text().startsWith("Acronym:")){
    					//Acronym: Hinc Prototype: HindII Org #: 1495 Organism: Haemophilus influenzae Rc Organism type: bacteria Organism source: ATCC 49699   (ATCC LINK) Growth Temperature: 37 ° Experimental Evidence: biochemistry Enzyme gene cloned Enzyme gene sequenced Crystal data present Molecular Weight: 29914
    		    		//System.out.println(link.text()); 
    		    		String[] tmpArray = link.text().split(":");
    		    		String sTag = "";
    		    		String tTag = "";
    		    		String wTag = "";
    		    		for(int k=0; k<tmpArray.length-1;k++){
    		    			String [] tmpArray1 = tmpArray[k].split("\\s+");
    		    			String [] tmpArray2 = tmpArray[k+1].split("\\s+");
    		    			if(tmpArray1[tmpArray1.length-1].equals("#")){
    		    				sTag = tmpArray2[1];
    		    			}
    		    			if(tmpArray1[tmpArray1.length-1].equals("Temperature")){
    		    				tTag = tmpArray2[1];
    		    			}
    		    			if(tmpArray1[tmpArray1.length-1].equals("Weight")){
    		    				wTag = tmpArray2[1];
    		    			}
    		    		}
    		    		outStr=outStr+sTag+";"+tTag+";"+wTag+";";
    				}
    				i++;
    			}
    			if(link.attributes().toString().equals(" size=\"1\"")){
    				if(link.text().toString().startsWith("# sites on")){
    					//# sites on Adeno2: 25 Lambda: 35 pBR322: 2 PhiX174: 13 SV40: 7
    					String[] tmpArray = link.text().split(":");
    					String Tag1 = "";
    					String Tag2 = "";
    					String Tag3 = "";
    					String Tag4 = "";
    					String Tag5 = "";
    		    		for(int k=0; k<tmpArray.length-1;k++){
    		    			//System.out.println(tmpArray[k]);
    		    			String [] tmpArray1 = tmpArray[k].split(" ");
    		    			String [] tmpArray2 = tmpArray[k+1].split(" ");
    		    			if(tmpArray1[tmpArray1.length-1].equals("Adeno2")){
    		    				if(!tmpArray2[1].equals("Lambda")){
    		    					Tag1 = tmpArray2[1];
    		    				}   		    				
    		    			}
    		    			if(tmpArray1[tmpArray1.length-1].equals("Lambda")){
    		    				if(!tmpArray2[1].equals("pBR322")){
    		    					Tag2 = tmpArray2[1];
    		    				}
    		    			}
    		    			if(tmpArray1[tmpArray1.length-1].equals("pBR322")){
    		    				if(!tmpArray2[1].equals("PhiX174")){
    		    					Tag3 = tmpArray2[1];
    		    				}
    		    			}
    		    			if(tmpArray1[tmpArray1.length-1].equals("PhiX174")){
    		    				if(!tmpArray2[1].equals("SV40")){
    		    					Tag4 = tmpArray2[1];
    		    				}
    		    			}
    		    			if(tmpArray1[tmpArray1.length-1].equals("SV40")){
    		    				Tag5 = tmpArray2[1];
    		    			}
    		    		}
    		    		outStr=outStr+Tag1+";"+Tag2+";"+Tag3+";"+Tag4+";"+Tag5+";";
    		    		sitesTag = true;
    				}
    				j++;
    			}
    	    }
    		if(!sitesTag){
    			outStr=outStr+";;;;;";
    		}
    		//System.out.println(outStr);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return outStr;
    }
}
