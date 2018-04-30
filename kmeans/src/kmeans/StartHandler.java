package kmeans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/*//Importar las clases necesarias de la librer√≠a de Weka
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;*/ 

public class StartHandler implements ActionListener {
	public GUI ref;
	//Constructor de la clase principal
	StartHandler(GUI x){
		super();
		//Guarda la referencia al frame principal
		ref=x;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int resp=-1;
		if(Integer.parseInt(ref.txtfClassifierAttribute.getText())!=ref.dat.classifierAttribite&&
				ref.dat.classifierAttribite!=-1)
			resp=JOptionPane.showConfirmDialog(ref,"Classifier attribute selected and saved are not equal.\nDo you want to change it?", "Index error", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(resp==0)			
			ref.dat.setClassifierAttribite(Integer.parseInt(ref.txtfClassifierAttribute.getText()));
		//Valida que los campos no esten vacios y que exista un archivo seleccionado
		if(!ref.txtfFile.getText().equals("None selected file"))			
			if(!ref.txtfClusters.getText().equals(""))
				//Verifica el tipo de implementaciona a utilizar
				if(ref.rdbtnOwn.isSelected())
					ownClustering();
				else
					wekaKNN();
			else
				JOptionPane.showMessageDialog(ref,"ERROR: Cross validation field is void");			
		else
			JOptionPane.showMessageDialog(ref,"ERROR: None selected file");		
	}
	
	private void ownClustering() {
		Kmeans kmns=new Kmeans(ref.dat.attributes,ref.dat.instances,ref.dat.classifierAttribite);	
		kmns.buildClusters(Integer.parseInt(ref.txtfClusters.getText()));
		int i=1;
		for(Cluster value:kmns.clusters) {
			ref.txtaConsole.setText(ref.txtaConsole.getText()+"*************************\n"
															 +"Grupo "+i+" Elementos:"+value.instances.size()+"\n");
															
			for(ArrayList<String> str:value.instances)
				ref.txtaConsole.setText(ref.txtaConsole.getText()+str+"\n");
			i++;
		}
	}

	private void ownCrossValidation(){		
		/*
		if(ref.chckbxSelectAttributes.isSelected()) {
			ref.txtaConsole.setText(ref.txtaConsole.getText()+"###############################################\n"
														 	 +"Atributos originales:\n"
														 	 +ref.dat+"\n");
			Backwards bkw=new Backwards(ref.dat,Integer.parseInt(ref.txtfCVFolds.getText()));		
			ref.dat=new Data(bkw.selectAtributes());
			ref.txtaConsole.setText(ref.txtaConsole.getText()+"Mejor seleccion de Atributos:\n"
				 	 										 +ref.dat+"\n");
		}
		//Imprime el mensaje del nombre del conjunto en el TextArea
		ref.txtaConsole.setText(ref.txtaConsole.getText()+"###############################################\n"
														 +"###############################################\n"
														 +" Nombre del conjunto: "+ref.dat.name+"\n\n");
		//Define las variables a utilizar:
		//Numero de partcicones a considerar para Cross Validation
		int folds=Integer.parseInt(ref.txtfCVFolds.getText());
		//Numero total de instancias del conjunto
		int m=ref.dat.instances.size();
		//Numero de instancias por particion
		int s=m/folds;
		//Inices que indican el inicio y fin del conjunto de prueba
		int p=0,q=s;
		//
		
		double porcent=0;
		//System.out.println(s);
		//Crea dos arraylist para almacenar el subconjunto de prueba y entenamiento
		ArrayList<ArrayList<String>> training=new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> test=new ArrayList<ArrayList<String>>();
		
		Measure ms=new Measure(ref.dat.attributes.get(ref.dat.classifierAttribite));
		//Inicia un ciclo desde 0 hasta folds (particiones de cross validation)
		for(int i=0;i<folds;i++){
			//Se claculan los subconjuntos correspondientes al conjunto de prueba y entrenamiento
			//para cada iteracion del Cross Validation
			//System.out.println(p+" "+q);
			//Si el puntero p es diferente de cero el conjunto de entrenamiento 
			//es un subconjunto desde 0 a p-1 del conjunto total
			if(p!=0)
				training.addAll(ref.dat.instances.subList(0,p));		
			//El conjunto de prueba es el subconjunto desde p a q-1 del conjunto total
			test.addAll(ref.dat.instances.subList(p,q));
			//Si q es diferente de m entonces aÒadir el subconjunto de q a m-1 del conjunto total
			//al subconjuto de entrenamineto
			if(q!=m)
				training.addAll(ref.dat.instances.subList(q,m));
			//Si el modulo de divicion entre el numero total de instacias y el numero de particiones
			//es diferente de cero, la ultima iteracion tomara el resto de iteracioes no considerado 
			//como parte del conjunto de prueba.
			//En caso contrario solo se le suma un incremento de s a los punteros q y p
			if(m%folds!=0&&i==folds-2)
				q=m;
			else
				q+=s;
			p+=s;
			//Imprime en TextArea cosola el conjunto de entrenamiento considerado
			ref.txtaConsole.setText(ref.txtaConsole.getText()+
									"********** Conjunto de Entrenamiento "+(i+1)+" *********\n");
			ref.txtaConsole.setText(ref.txtaConsole.getText()+
									training+"\n");
			
			//System.out.println(training+" "+ training.size());
			//Cada instancia del conjunto de pruba se envia a la funcion ownKNN junto con el conjunto 
			//de entrenamieto, para calcular su clasificacon estimada
			double aciertos=0;
			NB nb=new NB(ref.dat.attributes,training,ref.dat.classifierAttribite);		
			for(int j=0;j<test.size();j++){								
				String resp=nb.getDecision(test.get(j));
				ms.addMeasure(test.get(j).get(ref.dat.classifierAttribite), resp);
				//Si la clase calculada coincide con la clase de la instancia, incrementamos los aciertos
				if(resp.equals(test.get(j).get(ref.dat.classifierAttribite)))
					aciertos+=1;
				//Imprime en el text area consola la inctancia junto con su clase calculada 
				ref.txtaConsole.setText(ref.txtaConsole.getText()+"Test "+j+" "+test.get(j)+
										" Respuesta: "+resp+"\n");								
			}
			//Calcula el porcentaje de aciertos del conjunto de prueba
			double porc=((aciertos/test.size())*100);
			//Imprime el porcentaje de aciertos para el par i de conjunto de entrenamiento y prueba
			ref.txtaConsole.setText(ref.txtaConsole.getText()+"Aciertos de test "+
									(i+1)+": "+String.format("%.0f",aciertos)+" Porcentaje: "+String.format("%.2f",porc)+"%\n\n");
			//Se suma al porcentaje global del conjunto
			porcent+=porc;
			//System.out.println(training+" "+ training.size());
			//limpiamos el conjutno de prueba y entrenamiento
			training.clear();
			test.clear();
		}
		//Calculamos el promedio de los porcentajes obtenidos para obtener el porcentaje de exactitud del
		//algoritmo en este conjunto de instancias, y se imprime en el Text Area consola
		double accuracy=porcent/folds;
		ref.txtaConsole.setText(ref.txtaConsole.getText()+"\nExactitud: "+String.format("%.2f",accuracy)+"%\n\n\n");
		
		//Imprime Kappa
		ref.txtaConsole.setText(ref.txtaConsole.getText()+"\nKappa: "+String.format( "%.4f",ms.getKappa()));
		//Imprime Kappa
		ref.txtaConsole.setText(ref.txtaConsole.getText()+"\nMean Absolute Error: "+String.format( "%.4f",ms.getMAE())+"\n");
		
		//Imprime los parametros de exactitud
		ref.txtaConsole.setText(ref.txtaConsole.getText()+"TP Rate   FP Rate   Precision   F Measure\n");
		for(String value:ref.dat.attributes.get(ref.dat.classifierAttribite).range) {
			ref.txtaConsole.setText(ref.txtaConsole.getText()+String.format( "%.3f",ms.getTPRate(value))+"        ");
			ref.txtaConsole.setText(ref.txtaConsole.getText()+String.format( "%.3f",ms.getFPRate(value))+"        ");
			ref.txtaConsole.setText(ref.txtaConsole.getText()+String.format( "%.3f",ms.getPrecision(value))+"        ");
			ref.txtaConsole.setText(ref.txtaConsole.getText()+String.format( "%.3f",ms.getFMeasure(value))+"\n");
		}
		
		//Imprime la matriz de confusion
		ref.txtaConsole.setText(ref.txtaConsole.getText()+"\n"+ms.getMatrix()+"\n");
		
		*/
	}

	private void wekaKNN(){		
		/*try {
			//Crea un conjunto de datos
			Instances data = new Instances(ref.dat.getBufferedReader());
			//Define el atriuto con la clase
			data.setClassIndex(data.numAttributes()-1);
			//Crea un objeto de tipo KNN
			IBk knn=new IBk();
			//Sele eestablece el numeo de vecinos a considerar
			knn.setKNN(Integer.parseInt(ref.txtfKnn.getText()));
			//Crea un objeto de tipo clasificador y se le asigna el conjunto de datos
			Classifier ibk=new IBk();
			ibk.buildClassifier(data);	
			
			//Se crea una evaluacion		    
		    Evaluation eval = new Evaluation(data);
		    //Establece el modelo de validacion cruzada que se va a usar
		    eval.crossValidateModel(ibk,data, Integer.parseInt(ref.txtfCVFolds.getText()), new Random(1));
		    //Imprime en consola el resultado de la evaluacion
		    ref.txtaConsole.setText(ref.txtaConsole.getText()+
		    						"###############################################\n"+
					 				"###############################################\n"+
		    						"\nCalculo con clases de Weka\n"+
					 				"\nExactitud: "+Double.toString(eval.pctCorrect())+"%\n");    
			
		} catch (IOException e) {			
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}

