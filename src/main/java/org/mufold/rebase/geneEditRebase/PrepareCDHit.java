package org.mufold.rebase.geneEditRebase;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

public class PrepareCDHit {
	
	void readgenerateSeqClusters(String infileName, String outdir){
		try{
			
			List<String> al = FileUtils.readLines(new File(infileName));
			List<String> outAl = new ArrayList<String> (); 
			boolean flag = false;
			String filename = outdir+"0.txt";
			for(String str:al){
				//System.out.println(str);
				String[] tmpArray = str.split("\\s+");
				if(str.startsWith(">") && tmpArray[1].equals("0")){
										
				}else if(str.startsWith(">")){
					FileUtils.writeLines(new File(filename), outAl);
					outAl.clear();
					filename = outdir+tmpArray[1]+".txt";
				}else{
					outAl.add(tmpArray[2].split("\\.")[0].substring(1));
				}
			}
			FileUtils.writeLines(new File(filename), outAl);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/*
	public static void main(){
		PrepareCDHit app = new PrepareCDHit();
		//use faSomeRecords
		//app.readgenerateSeqClusters("E:\\REBASE\\CDHIT\\1478816416.fas.1.clstr.sorted", "E:\\REBASE\\CDHIT\\out\\");
	}
	*/

}
