import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI{
	
	private Client client;
	
	private JFrame frame;
	private JButton search;
	private JTextField word;
	
	private Listener listener = new Listener();
	
	// lista de resultados, a esquerda
	private DefaultListModel<SearchResult> files = new DefaultListModel<SearchResult>();
	private JList<SearchResult> filesList = new JList<SearchResult>(); // files
	private JScrollPane scroll;
	
	private JTextArea area; // texto do resultado seleccionado, a direita
	
	public GUI(Client client){
		this.client = client;
		buildGUI();
	}
	
	private void buildGUI(){
		frame = new JFrame("ISCTE Searcher");
		frame.setSize(1000, 700);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		
		frame.add(painelNorte(), BorderLayout.NORTH);
		frame.add(painelCentro(), BorderLayout.CENTER);
	}
	
	private JPanel painelNorte(){
		JPanel painel = new JPanel(new FlowLayout());
		
		word = new JTextField(30);
		painel.add(word);
		
		search = new JButton("Search");
		search.addActionListener(listener);
		painel.add(search);
		
		return painel;
	}
	
	private JPanel painelCentro(){
		JPanel painel = new JPanel(new GridLayout(1, 2));
		
		filesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filesList.addListSelectionListener(listener);
		scroll = new JScrollPane(filesList);
		scroll.setSize(500, 700);
		painel.add(scroll);
		
		area = new JTextArea();
		area.setEditable(false);
		painel.add(new JScrollPane(area));
		
		return painel;
	}
	
	private class Listener implements ListSelectionListener, ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == search){
				String wordText = word.getText();
				if(wordText.equals("")){
					JOptionPane.showMessageDialog(frame, "Null expression!");
				} else{					
					System.out.println("Looking for: " + wordText);
					procurar(wordText);
				}
			}
		}

		public void valueChanged(ListSelectionEvent e){
			if(e.getSource() == filesList){
				if(!e.getValueIsAdjusting()){
					if(filesList.isSelectionEmpty()){
						area.setText("");
					} else{
						SearchResult noticia = filesList.getSelectedValue();
						SearchFile file = noticia.getFile();
						area.setText(file.getTitle() + "\n\n" + file.getText());
						area.setLineWrap(true); //
						area.setWrapStyleWord(true); //
					}
				}
			}
		}	
	}
	
	private void procurar(String word){
		filesList.clearSelection(); // !!!
		area.setText("");
		files.clear();
		
		try{
			client.sendRequest(word);
			System.out.println("Searching...");
		} catch(IOException e){
			System.err.println("ERROR GUI send request");
		}
		
	}
	
	public void addResponse(SearchResponse response) throws IOException{	
//		files.clear();
//		area.setText("");
	
		ArrayList<SearchResult> results = response.getResults();
		int n1 = results.size();
		System.out.println(n1 + " files searched!");
		
		ArrayList<SearchResult> finalList = new ArrayList<SearchResult>();
		
		for(SearchResult result : results)
			if(result.getN() != 0)
				finalList.add(result);
		
		int n2 = finalList.size();
		System.out.println(n2 + " files matched!");
		
		Collections.sort(finalList);
		
		for(SearchResult result : finalList){
			files.addElement(result);
//			System.out.println(result); // debug
		}
		
		filesList.setModel(files);
		filesList.clearSelection();
		filesList.repaint();
		
	}
	
	public void init(){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
