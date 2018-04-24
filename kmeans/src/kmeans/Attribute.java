package kmeans;

import java.util.ArrayList;

public class Attribute {
	//Almacena el nombre del atributo
	public String name;
	//Alamacena los posibles valores para el atributo
	public ArrayList<String> range;
	//Almacena el indece de atributo 
	public int index;
	
	//Constructores de la clase
	Attribute(){}
	Attribute(Attribute e){
		this.name=e.name;
		this.range=new ArrayList<String>(e.range);
		this.index=e.index;
	}
	Attribute(String n, ArrayList<String> r,int i){
		this.name=n;
		this.range=r;
		this.index=i;
	}
	
	@Override
	public String toString(){ 
		String resp=name+" {";
		for(int i=0;i<range.size()-1;i++)
			resp+=range.get(i)+",";
		resp+=range.get(range.size()-1)+"}";
		return resp;		
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals (Object o) {
        if (o instanceof Attribute) 
        	if(this.name.equals(((Attribute) o).name))
        		return true;
        	else
        		return false;
        else
        	return false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getRange() {
		return range;
	}

	public void setRange(ArrayList<String> range) {
		this.range = range;
	}
	
	
}
