package kmeans;

import java.util.ArrayList;

public class Kmeans {
	ArrayList<Attribute> attributes;
	ArrayList<ArrayList<String>> instances;
	int classifierAttribute;
	Kmeans(ArrayList<Attribute> attributes,ArrayList<ArrayList<String>> instances,int classifierAttribute){
		this.attributes=attributes;
		this.instances=instances;
		this.classifierAttribute=classifierAttribute;		
	}
	public void buildClusters(int numClusters) {
		
	}
}
