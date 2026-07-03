import java.io.Serializable;

@SuppressWarnings("serial")
public class SearchFile implements Serializable{
	
	private String title; // 1a linha
	private String text; // 2a linha, conteudo da noticia
	private int id; // !!!
	
	public SearchFile(String title, String text, int id){
		this.title = title;
		this.text = text;
		this.id = id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getText(){
		return text;
	}
	
	public String getContent(){
		return (title + " " + text);
	}
	
	public int getID(){
		return id;
	}

}
