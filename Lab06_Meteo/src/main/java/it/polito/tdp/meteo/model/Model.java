package it.polito.tdp.meteo.model;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO meteoDAO;
	private List<Rilevamento>rilevamenti;
	private int bestCosto;
	private List<Citta>bestSoluzione;
	public Model() 
	{
		meteoDAO= new MeteoDAO();
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		String risultati="";
		List<Rilevamento> listaRil=meteoDAO.getAllRilevamenti();
		Map<String,Citta> mappaCitta=new TreeMap<>();
		for(Rilevamento r:listaRil)
		{
			Citta c;
			if(!mappaCitta.containsKey(r.getLocalita()))
			{	
				c=new Citta(r.getLocalita());
				mappaCitta.put(c.getNome(), c);
			}
			else
			{
				c=mappaCitta.get(r.getLocalita());
			}
			Date date = r.getData();
			/*LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int month = localDate.getMonthValue();*/
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
		    int month=Integer.parseInt(dateFormat.format(date));
			if(mese==month)
			{
				c.addRilevamento(r);
				c.increaseCounter();
			}
		}
		for(String s: mappaCitta.keySet())
		{
			float media=mappaCitta.get(s).media();
			risultati+=s+" "+media+"\n";
		}
		return risultati;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		 Citta Torino = new Citta ("Torino", meteoDAO.getAllRilevamentiLocalitaMese(mese, "Torino"));
		 Citta Milano = new Citta ("Milano", meteoDAO.getAllRilevamentiLocalitaMese(mese, "Milano"));
		 Citta Genova = new Citta ("Genova", meteoDAO.getAllRilevamentiLocalitaMese(mese, "Genova"));
		 
		 List <Citta> citta = new ArrayList <>();
		 citta.add(Genova);
		 citta.add(Milano);
		 citta.add(Torino);
		 
		 bestCosto=0;
		 bestSoluzione = new ArrayList <>();
		 List <Citta> parziale = new ArrayList <>();
		 
		 cerca(parziale, 0, citta);
		
		return bestSoluzione;
	}
	
	private void cerca(List <Citta> parziale, int livello, List <Citta> citta) {
		
		//caso terminale 
		if (parziale.size()==NUMERO_GIORNI_TOTALI) {
			if (bestCosto==0 ) {
				this.bestSoluzione= new ArrayList <>(parziale);
				bestCosto = this.calcolaCosto(parziale);
			}
			else if  (this.calcolaCosto(parziale)<bestCosto){
				this.bestSoluzione= new ArrayList <>(parziale);
				bestCosto = this.calcolaCosto(parziale);
			
			}
		}
		else {
		//caso intermedio 
		for (int i=0; i<citta.size(); i++) {
			int flag=0;
			Citta c = citta.get(i);
			if (c.getCounter()>=NUMERO_GIORNI_CITTA_MAX) {
				flag=1;
			}
			
			if (flag==0) {
				citta.get(i).increaseCounter();
				parziale.add(c);
				cerca (parziale, livello+1, citta);
				parziale.remove(c);
				citta.get(i).setCounter(c.getCounter()-1);
			}
				
			}
		}
	}
		
			
	
	
	public int calcolaCosto (List <Citta> sequenza) {
		int costo=0;
		
		for (int i=0; i<sequenza.size(); i++) {
			if (i>=1) { 
				if (sequenza.get(i).getNome().compareTo(sequenza.get(i-1).getNome())!=0) {
				costo += COST;
				}
			}
			costo += sequenza.get(i).getRilevamenti().get(i).getUmidita();
		}
			
		return costo;
	}
	
	/*public boolean controllo (List<Rilevamento> quindiciGiorni){
		return false;
		
	}*/
	
	public int convertiMese(String meseString)
	{
		int mese = 0;
		switch(meseString)
		{
			case "Gennaio":
			{
				mese=1;
				break;
			}
			case "Febbraio":
			{	
				mese=2;
				break;
			}
			case "Marzo":
			{
				mese=3;
				break;
			}
			case "Aprile":
			{
				mese=4;
				break;
			}
			case "Maggio":
			{	
				mese=5;
				break;
			}
			case "Giugno":
			{
				mese=6;
				break;
			}
			case "Luglio":
			{	
				mese=7;
				break;
			}
			
			case "Agosto":
			{	
				mese=8;
				break;
			}
			case "Settembre":
			{	
				mese=9;
				break;
			}
			case "Ottobre":
			{
				mese=10;
				break;
			}
			case "Novembre":
			{	
				mese=11;
				break;
			}
			case "Dicembre":
			{
				mese=12;
				break;
			}
		}
		return mese;
	}
	/* private void ricerca(List<Citta>parziale, int L, List<Citta> disponibili)
	 {
		if(parziale.size()==this.NUMERO_GIORNI_TOTALI)
		{
			int costo=this.costoRilevamenti(parziale);
			if(this.bestCosto>costo || this.bestCosto==0)
			{
				this.bestSoluzione=new ArrayList<>(parziale);
				this.bestCosto=costo;
			}
		}
		
		
		// lo aggiungo
		for(int i=0; i<disponibili.size();i++)
		{
			Citta c=disponibili.get(i);
			if(c.getCounter()>this.NUMERO_GIORNI_CITTA_MAX)
			{
				return;
			}
			c.increaseCounter();
			parziale.add(c);
			this.ricerca(parziale, L+1, disponibili);
		}
	 }*/
	
	
}
