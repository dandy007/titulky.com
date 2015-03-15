package org.danysoft.lib.web_content_provider.Titulky_com.pages;

public class TitulkyPage extends WebPage {

  protected static final String TITULKY_URL = "http://www.titulky.com";
  
  public TitulkyPage(String url) {
    super(TITULKY_URL + url);
  }
  
}
