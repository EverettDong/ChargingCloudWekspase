package com.cpit.icp.collect.process;

public class CpSequenceDto {
private int flowNum=0;
private int sequenceNum=0;

public int getFlowNum() {
	++flowNum;
	if(flowNum>65535) {
		flowNum=1;
	}
	return flowNum;
}

public int getSequenceNum() {
	++sequenceNum;
	if(sequenceNum>40) {
		sequenceNum=1;
	}
	return sequenceNum;
}

@Override
public String toString() {
	return "CpSequenceDto [flowNum=" + flowNum + ", sequenceNum=" + sequenceNum + "]";
}



public static void main(String[] args) {
	CpSequenceDto cpSeq = new CpSequenceDto();
	for(int i=0;i<50;i++) {
		System.out.println(cpSeq.getFlowNum());
		System.out.println(cpSeq.getSequenceNum());
		System.out.println(cpSeq.toString());
	}
	
}

}
