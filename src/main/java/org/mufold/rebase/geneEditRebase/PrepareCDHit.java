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
	
	void readgenerateSeqClustersSelf(String seqfileName, String infileName, String outdir){
		try{
			
			List<String> al = FileUtils.readLines(new File(infileName));
			HashMap hm = new HashMap();
			int clusterNum = 0;
			for(String str:al){
				//System.out.println(str);
				String[] tmpArray = str.split("\\s+");
				if(str.startsWith(">") && tmpArray[1].equals("0")){
										
				}else if(str.startsWith(">")){
					clusterNum = Integer.parseInt(tmpArray[1]);					
				}else{
					hm.put(tmpArray[2].substring(0, tmpArray[2].length()-3).substring(1),clusterNum);
				}
			}
			
			getSingleSeq(seqfileName, hm, clusterNum, outdir);
						
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	void getSingleSeq(String seqfileName, HashMap hm, int clusterNum, String outdir){
		
		try{
		ArrayList outList = new ArrayList();
		for(int i=0;i<=clusterNum;i++){
			outList.add("");
		}
		
		List<String> inList = FileUtils.readLines(new File(seqfileName));
		List<String> seqList = new ArrayList<String> ();
		String seq = "";
		int count = 0;
		
		for(String inStr:inList){
			String[] tmpArray = inStr.split("\\s+");
			if(inStr.startsWith(">") && count == 0){
				seq = tmpArray[0]+" \n";
				
			}else if(inStr.startsWith(">")){
				seqList.add(seq);
				seq = tmpArray[0]+" \n";
				
			}else if(inStr.length() == 0){
				
			}
			else{
				for(int i=0; i<tmpArray.length; i++){
					seq = seq+tmpArray[i];
				}
				count++;				
			}
		}
		
		
		for(String seqq:seqList){
			String name = seqq.split(" ")[0].substring(1);
			//System.out.println(name);
			//System.out.println(seqq+"\n");
            int num = Integer.parseInt(hm.get(name).toString());
            String tmpstr = outList.get(num).toString();
            tmpstr = tmpstr + seqq;
            outList.set(num, tmpstr+"\n");
			
		}
		
		/*
		LinkedHashMap<String, ProteinSequence> a = FastaReaderHelper.readFastaProteinSequence(new File(seqfileName));
        
        for (Entry<String, ProteinSequence> entry : a.entrySet()) {
            String name = entry.getValue().getOriginalHeader().toString().split("\\s+")[0].substring(1);
            int num = Integer.parseInt(hm.get(name).toString());
            String tmpstr = outList.get(num).toString();
            tmpstr = tmpstr + ">" + name + "\n" + entry.getValue().getSequenceAsString() + "\n";
            outList.set(num, tmpstr);
        }
        */
        
        
        for(int i=0;i<=clusterNum;i++){
        	FileWriter fw = new FileWriter(new File(outdir+ new Integer(i).toString()+".txt"));
            fw.write(outList.get(i).toString());
            fw.close();       	
        }
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
