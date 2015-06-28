package Interfaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import EstructuraXML.Aplicacion;
import EstructuraXML.Parametro;
import EstructuraXML.SubAplicacion;
import EstructuraXML.Tag;
import Parser.CPoolXMLHandler;
import Parser.Parser;

import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VentanaSwing extends JFrame {

	JPanel contentPane;
	JPanel refPanelListaApps = new JPanel();
	JPanel refPanelListaSubApps = new JPanel();
	JPanel refPanelListaParametros = new JPanel();
	JButton refBotonComando = new JButton("Generar comando");
	String comandoParcial = new String("");
	Parser parser = new Parser();
	CPoolXMLHandler handler = new CPoolXMLHandler();
	List<ValidadorParametro> validadores= new ArrayList<ValidadorParametro>();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaSwing frame = new VentanaSwing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaSwing() {
		super("JCmdTool: Java Command Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		
		parser.setHandler(handler);
	    parser.parsearXml("algoritmos2_tp.xml");
	    List<Aplicacion> apps = new ArrayList<Aplicacion>();
	    apps.addAll(handler.getContenidoXml());
	    
		//setLayout(new FlowLayout());
	    //Container contentPane = getContentPane();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(3, 1));
		setContentPane(contentPane);
		contentPane.add(refPanelListaApps, 0);
		contentPane.add(refPanelListaSubApps, 1);
		contentPane.add(refPanelListaParametros, 2);
		
		this.generarPanelAplicaciones(apps);
	}
	  	 //System.out.println(appElegida.getValor());
	  	 //System.out.println(appElegida.getSubTags().size());

	public void generarPanelAplicaciones(List<Aplicacion> apps) {
		 //Panel Aplicaciones
		 contentPane.remove(refPanelListaApps);
		 JPanel panelListaApps = new JPanel();
		 panelListaApps.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		 panelListaApps.setLayout(null);
		 JLabel label = new JLabel("Aplicaciones :");
		 label.setBounds(25, 10, 65, 14);
		 panelListaApps.add(label);
		 Vector<String> aplicaciones = new Vector<String>();
		 for(int i = 0; i < apps.size(); i++){
			 aplicaciones.addElement(apps.get(i).getValor());
		 }
		 JComboBox<String> comboBoxApps = new JComboBox<String>(aplicaciones);
		 comboBoxApps.setBounds(100, 10, 144, 20);
		 comboBoxApps.setSelectedIndex(3);
		 //System.out.println(comboBoxApps.getSelectedItem());
		 panelListaApps.add(comboBoxApps);
		 panelListaApps.setVisible(true);
		 comboBoxApps.addItemListener(new EventoSubAplicaciones(this, apps, comboBoxApps));
		 refPanelListaApps = panelListaApps;
		 contentPane.add(refPanelListaApps, 0); 

	}

	public void generarPanelSubAplicaciones(Aplicacion appElegida) {
		contentPane.remove(refPanelListaSubApps);
		//Panel SubApps
		JPanel panelListaSubApps = new JPanel();
		JLabel label = new JLabel("Sub-Aplicaciones :");
		label.setBounds(25, 60, 65, 14);
		panelListaSubApps.add(label);
		//panelListaSubApps.setLayout(new FlowLayout());
		panelListaSubApps.setLayout(null);
		Vector<String> subApps = new Vector<String>();
		for(int i = 0; i < appElegida.getSubAplicacioes().size(); i++){
				 subApps.addElement(appElegida.getSubAplicacioes().get(i).getValor());
		}
		JComboBox<String> comboBoxSubApps = new JComboBox<String>(subApps);
		comboBoxSubApps.setBounds(100, 60, 144, 20);
		comboBoxSubApps.setSelectedIndex(0);
		//System.out.println(comboBoxApps.getSelectedItem());
		panelListaSubApps.add(comboBoxSubApps);
		panelListaSubApps.setVisible(true);
		comboBoxSubApps.addItemListener(new EventoParametros(this, appElegida, comboBoxSubApps));
		refPanelListaSubApps = panelListaSubApps;
		contentPane.add(panelListaSubApps, 1);
	}
	

	public void generarPanelParametros(SubAplicacion subAppElegida, String nombreSubAppElegida) {
		contentPane.remove(refPanelListaParametros);
		List<Parametro> parametros = new ArrayList<Parametro>();
		parametros = subAppElegida.getParametros();
		
		//Panel Parametros
		JPanel panelListaParametros = new JPanel();
		panelListaParametros.setLayout(new FlowLayout());
		JLabel label = new JLabel("Par�metros:");
		panelListaParametros.add(label);
		String auxS;
		int auxInt = 110;

		for(int i = 0; i < parametros.size(); i++){
			 auxS = parametros.get(i).getValor();
			 auxInt = auxInt + 50;
			 JLabel labelAux = new JLabel(auxS);
			 panelListaParametros.add(labelAux);
			 JTextField texto = new JTextField(20);
			 panelListaParametros.add(texto);
			 setJTexFieldChanged(texto);
			 ValidadorParametro validador = new ValidadorParametro(texto, parametros.get(i));
			 validadores.add(validador);
		}
		this.generarBotonCmd();
		refPanelListaParametros = panelListaParametros;
		contentPane.add(panelListaParametros, 2);
	}
	
	public void generarBotonCmd(){
		contentPane.remove(refBotonComando);
		JButton botonComando = new JButton("Generar comando");
		botonComando.addActionListener(new EventoGenerarCmd(this.generarCmd()));
		refBotonComando = botonComando;
		contentPane.add(refBotonComando);
	}

	public String generarCmd() {
		String comandoAux;
		comandoAux = (String) ((JComboBox<String>) refPanelListaApps.getComponent(1)).getSelectedItem();
		comandoAux = comandoAux+" "+(String) ((JComboBox<String>) refPanelListaSubApps.getComponent(1)).getSelectedItem();
		//System.out.println("EL cmd parcial: "+comando+comandoParcial );
		return comandoAux+comandoParcial;
	}

	private void textoParametros() {
		this.reiniciarComandoParcial();
		int i = 0;
		while(i < validadores.size()){
			comandoParcial = comandoParcial+" "+(String) validadores.get(i).generarCmd();
			i++;
		}
		//System.out.println("Texto: "+textoParametros);
	}
	
    private void setJTexFieldChanged(JTextField txt)
    {
        DocumentListener documentListener = new DocumentListener() {
 
        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            printIt(documentEvent, txt);
        }
 
        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            printIt(documentEvent, txt);
        }
 
        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            printIt(documentEvent, txt);
        }
        };
        txt.getDocument().addDocumentListener(documentListener);
 
    }
 
    private void printIt(DocumentEvent documentEvent, JTextField txt) {
        DocumentEvent.EventType type = documentEvent.getType();
 
        if (type.equals(DocumentEvent.EventType.CHANGE))
        {
        	//refBotonComando.removeAll();
        	this.textoParametros();
        	//System.out.println("Change: " + comandoParcial);
        	this.generarBotonCmd();
        }
        else if (type.equals(DocumentEvent.EventType.INSERT))
        {
        	//refBotonComando.removeAll();
        	this.textoParametros();
        	//System.out.println("Instert: " + comandoParcial);
        	this.generarBotonCmd();
        }
        else if (type.equals(DocumentEvent.EventType.REMOVE))
        {
        	//refBotonComando.removeAll();
        	this.textoParametros();
        	//System.out.println("Remove: " + comandoParcial);
        	this.generarBotonCmd();
        }
   }

	public void reiniciarComandoParcial() {
		comandoParcial = new String("");
	}
	
	public void reiniciarValidadores() {
		validadores.clear();
	}
   
	
}