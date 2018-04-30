package kmeans;

import java.util.ArrayList;
//Class Cluster
/*Esta es a clase encargada de contener los grupos que se generan
 *para la implementación del algoritmo kmeans
 */
public class Cluster {
	ArrayList<String> centroid;//Almacena el centroide del clustes
	ArrayList<ArrayList<String>> instances;//Alamcena los elementos que pertenecen al cluster
	int classifierAttribute;//Indice del atributo clasificador
	int distanceType;/*Alamcena el tipo de distancia a utilizar.
	 				  * 1 para distancia Manhattan
	 				  * 2 Para distancia Euclidiana
	 				  */	
	//Constructor de la clase
	public Cluster(ArrayList<String> in,int classifierAttribute,int distance) {
		//Se construye a partir de un ArrayList como centroide
		this.centroid=new ArrayList<String>(in);
		this.classifierAttribute=classifierAttribute;
		this.instances=new ArrayList<ArrayList<String>>();
		/*Si el tipo de distancia a utilizar no esta definfido
		 *por defecto se le asigna la distancia euclideana
		 */
		if(distance==1||distance==2)
			this.distanceType=distance;
		else
			this.distanceType=2;
		
	}
	//Metodo para obtener la distacia entre el centroide y una instancia
	public double getDistance(ArrayList<String> instance) {
		double distance=0;
		//Compruba que la instancia y el centroide tengan la misma cantidad de atributos
		if(centroid.size()==instance.size()) {
			for(int i=0;i<centroid.size();i++) {
				//Salta el calculo del atributo clasificador
				if(i!=this.classifierAttribute) {
					//Transforma los atributos string a tipo duble
					double x1=Double.parseDouble(centroid.get(i));
					double x2=Double.parseDouble(instance.get(i));
					//Calcula la diferencia entre atributos
					distance+=Math.pow(Math.abs(x2-x1),this.distanceType);
				}
			}//En caso de que la distancia sea euclideana realiza la raiz cuadrada
			if(distanceType==2)
				distance=Math.sqrt(distance);
			return distance;
		}else
			return -1;
	}
	//Metodo que añade elementos al cluster
	public boolean addInstance(ArrayList<String> instance) {
		//Compruba que la instancia y el centroide tengan la misma cantidad de atributos
		if(centroid.size()==instance.size()) 			
			return (this.instances.add(instance));//Añade elementos a las instancias del cluster
		else
			return false;
	}
	//Metodo que actualiza los valores del centroide del cluster
	public void updateCentroid(){
		//Si existen elementos en el cluster
		if(!instances.isEmpty()) {
			//Calcula el promedio de cada atributo
			for(int i=0;i<instances.get(0).size();i++) {
				double sum=0;
				//Salta el atributo clasificador
				if(i!=this.classifierAttribute) {
					for(int j=0;j<instances.size();j++) {
						sum+=Double.parseDouble(instances.get(j).get(i));
					}//ajusta el divisor si hay atributo clasificador
					int div=(classifierAttribute!=-1)?instances.size()-1:instances.size();
					centroid.set(i,Double.toString(sum/div));
				}
			}					
		}		
	}
	//Metodo que retorna una copia de las instancias del cluster
	public ArrayList<ArrayList<String>> getInstances(){
		ArrayList<ArrayList<String>> resp=new ArrayList<ArrayList<String>>();
		for(ArrayList<String> value:this.instances)
			resp.add(new ArrayList<String>(value));
		return resp;
	}
	//Metodo que limpia los elementos del cluster
	public void clearInstances() {
		this.instances.clear();
	}
}
