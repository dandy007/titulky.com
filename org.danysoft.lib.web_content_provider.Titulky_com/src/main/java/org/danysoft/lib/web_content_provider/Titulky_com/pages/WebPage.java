package org.danysoft.lib.web_content_provider.Titulky_com.pages;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebPage {

  private URL url;
  private String stringUrl;
  private Document domDocument;
  
  public WebPage(String urlString) {
    this.stringUrl = urlString;
  }

  public void refresh(String params) throws IOException {
    assert stringUrl != null;
    url = new URL(stringUrl);
    if (params != null) {
      domDocument = Jsoup.connect(stringUrl + "?" + params).get().normalise();
    } else {
      domDocument = Jsoup.connect(stringUrl).get().normalise();
    }
  }
  
  public URL getUrl() {
    return url;
  }
  
  public Document getDOM(String params) throws IOException {
    if (domDocument == null) {
      refresh(params);
    }
    return domDocument;
  }
  
  @Override
  public String toString() {
    if (domDocument == null) return null;
    return stringUrl + "\n\n" + domDocument.toString();
  }
  
}
