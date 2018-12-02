package utils;

import java.util.ArrayList;
import java.util.List;

/* Java implementation to convert infix expression to postfix*/
//Note that here we use Stack class for Stack operations 

import java.util.Stack;

import entidades.ElementoPosfixa;
import enums.SimboloEnum; 

public class ConversorPosfixa 
{ 
	private List<ElementoPosfixa> elementos;
	private List<ElementoPosfixa> ordenacaoPosfixa;
	private List<ElementoPosfixa> ordenacaoAux;


	public ConversorPosfixa() {
		this.elementos = new ArrayList<>();
		this.ordenacaoPosfixa = new ArrayList<>();
		this.ordenacaoAux = new ArrayList<>();
	}
	
	public List<ElementoPosfixa> getOrdenacaoPosfixa() {
		return ordenacaoPosfixa;
	}

	public void constroiExpressao(ElementoPosfixa elemento) {
		this.elementos.add(elemento);
	}
	public String getExpressao() {
		String expressao = new String();
		for (ElementoPosfixa elementoPosfixa : elementos) {
			expressao = expressao +" "+elementoPosfixa.getToken().getLexema();
		}
		expressao +="\n";
		ordernacaoPosfixa();
		for (ElementoPosfixa elementoPosfixa : ordenacaoPosfixa) {
			expressao = expressao +" "+elementoPosfixa.getToken().getLexema();
		}
		return expressao;
	}
	
	public void ordernacaoPosfixa(){
		this.ordenacaoPosfixa = new ArrayList<>();

		while(elementos.size()>0) {
		
			if(elementos.get(0).getToken().getSimbolo().equals(SimboloEnum.Sidentificador)
					||elementos.get(0).getToken().getSimbolo().equals(SimboloEnum.Snumero)
					||elementos.get(0).getToken().getSimbolo().equals(SimboloEnum.Sverdadeiro)
					||elementos.get(0).getToken().getSimbolo().equals(SimboloEnum.Sfalso)) {
				ordenacaoPosfixa.add(elementos.get(0));
				elementos.remove(0);
				if(elementos.size()==0) {
					break;
				}
			}
			if(ordenacaoAux.size()==0) {
				ordenacaoAux.add(elementos.get(0));
				elementos.remove(0);
			} else if(ordenacaoAux.get(ordenacaoAux.size()-1).getPrioridade() < elementos.get(0).getPrioridade()
					|| elementos.get(0).getPrioridade()==-1) {
				ordenacaoAux.add(elementos.get(0));
				elementos.remove(0);
			} else if (ordenacaoAux.get(ordenacaoAux.size()-1).getPrioridade() >= elementos.get(0).getPrioridade()) {
				while(ordenacaoAux.get(ordenacaoAux.size()-1).getPrioridade() > elementos.get(0).getPrioridade()) {
					if(ordenacaoAux.get(ordenacaoAux.size()-1).getPrioridade() > -1) {
						ordenacaoPosfixa.add(ordenacaoAux.get(ordenacaoAux.size()-1));
					} else {
						//ponto de atenção
						elementos.remove(0);
					}
					ordenacaoAux.remove(ordenacaoAux.size()-1);
					if(elementos.size()==0
							||ordenacaoAux.size()==0) {
						break;
					}
		
				}
				if(elementos.size()>0) {
					ordenacaoAux.add(elementos.get(0));
					elementos.remove(0);
				}
			}
			
		}
		
		while(ordenacaoAux.size()>0) {
			ordenacaoPosfixa.add(ordenacaoAux.get(ordenacaoAux.size()-1));
			ordenacaoAux.remove(ordenacaoAux.size()-1);
		}
		//return this.ordenacaoPosfixa;
	}
	
} 
