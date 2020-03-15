package mundo.Navasoft;

import java.util.ArrayList;

public class Buffer {	
	//Variable que representa la capacidad 
	private int capacidad;
	//Variable que indican el n�mero de clientes que est� esperando 
	private int numeroClientes;
	//Buz�n donde se guardan los mensajes
	private ArrayList<Mensaje> buzon;
		
	public Buffer(int pCapacidad){
		//Se inicializa en la capacidad entrada por par�metro
		capacidad = pCapacidad;
		//Se inicializa vac�o el buz�n
		buzon= new ArrayList<>();
	}	
	public void setCapacidad(int pC){
		//Se cambia la velocidad del Buffer
		capacidad = pC;
	}
	public synchronized int getNumClientes(){
		// Numero de clientes que esperan respuesta
		return numeroClientes;
	}
	public synchronized void agregarCliente(){
		//Aumenta en 1 el n�mero de cliente
		numeroClientes++;
	}
	public synchronized void retirarCliente(){
		//Retira un cliente
		numeroClientes--;
	}
	public synchronized void enviarRespuesta(Mensaje pMen){
		//Como el hilo esta esperando sobre el mensaje, entonces notifico al mensaje que es donde espera el cliente
		synchronized (pMen) {			
			System.out.println("Un hilo servidor envi� la respuesta");
			//Notific� al cliente que esta esperando
			pMen.notify();
		}				
	}
	public synchronized void recibirMensaje(Mensaje pMensaje){			
		// Mientras se pueda agregar al buz�n se agregar� 
		while(buzon.size() >= capacidad){
			try{				
				System.out.println("Un cliente se va a dormir");
				//Si no puedo agregar espero
				wait();
				System.out.println("Un cliente se despierta");
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		//Si si puedo, agrego el mensaje al buz�n
		buzon.add(pMensaje);
	}
	public synchronized Mensaje retirarMensaje(){
		//Si el buz�n tiene mensajes, lo retirno
		if(!buzon.isEmpty()){			
			return buzon.remove(buzon.size()-1);
		}
		else{
			return null;
		}
	}
	public synchronized void notificar(){
		//Sirve para que el hilo servidor pueda notificar que ya hay mensaje menos en el buz�n
		notify();
	}
}