package org.danysoft.lib.web_content_provider.Titulky_com.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.danysoft.lib.web_content_provider.Titulky_com.data.TitleListRecord;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TitulkyNewPage extends TitulkyPage {
  
  private static final int PAGE_RECORD_COUNT = 50;
  
  private Map<String, List<TitleListRecord>> cache = new HashMap<String, List<TitleListRecord>>();

  private static final String TITULKY_NEW_URL = "/index.php?orderby=3&OrderDate=2";
  
  public TitulkyNewPage() {
    super(TITULKY_NEW_URL);
  }
  
  public List<TitleListRecord> getNewTitles() throws IOException {
    return getNewTitles(0);
  }
  
  public List<TitleListRecord> getNewTitles(int page) throws IOException {
    List<TitleListRecord> titles = cache.get(page+"");
    if (   titles == null 
        || titles.isEmpty()
    ) {
      refresh("&ActualRecord=" + (page * PAGE_RECORD_COUNT));
      titles = parseTitles();
      cache.put(page+"", titles);
    }
    return titles;
  }
  
  public List<TitleListRecord> getAllNewTitles() throws IOException {
    List<TitleListRecord> list = new ArrayList<TitleListRecord>();
    
    int pages = getNumberOfPages();
    list.addAll(getNewTitles());
    
    List<Thread> runnableList = new ArrayList<Thread>();
    
    for (int i = 1; i < pages; i++) {
      Thread t = new TitulkyNewThread(this, i, list);
      runnableList.add(t);
    }
    
    
    for (int i = 0; i < runnableList.size(); i++) {
      runnableList.get(i).start();
    }
    
    for (int i = 0; i < runnableList.size(); i++) {
      try {
        runnableList.get(i).join();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    return list;
  }

  public int getNumberOfPages() throws IOException {
    Elements pages = getDOM(null).select(".datalister .listeritem a");
    if (pages.size() <= 2) return 0;
    if (pages.size() > 22) return 20;
    return pages.size() - 2;
  }
  
  private List<TitleListRecord> parseTitles() throws IOException {
    List<TitleListRecord> list = new ArrayList<TitleListRecord>();
    
    Elements records = getDOM(null).select("#contcont table tbody tr ");
    boolean firstRecordSkiped = false;
    for (Element titleTR: records) {
      if (!firstRecordSkiped) {
        firstRecordSkiped = true;
        continue;
      }
      TitleListRecord record = new TitleListRecord();
      list.add(record);
      
      Elements fields = titleTR.getElementsByTag("td");
      int index = -1;
      for (Element titleTD: fields) {
        index++;
        
        String value = titleTD.text().trim();
        
        switch (index) {
          case 0: 
            if (!value.isEmpty()) { 
              record.setName(value); 
            }
            Elements aLinks = fields.select("a");
            if (aLinks.size() > 0) {
              String id = aLinks.get(0).attr("href");
              if (id != null) {
                record.setUrlId(TITULKY_URL+"/"+id);
              }
            }
          break;
          case 1: 
            Elements versionEls = titleTD.getElementsByTag("a");
            if (!versionEls.isEmpty()) {
              String version = versionEls.get(0).attr("title");
              if (!version.isEmpty()) {
                record.setVersion(version);
              }
            }
            
            break;
          case 2: 
            if (value.length() <= 1 ) break; 
            record.setSeriesName(value);
          break;
          case 3: 
            if (value.isEmpty()) break; 
            try {
              int year = Integer.valueOf(value);
              record.setYear(year); 
            } catch (NumberFormatException ne) {}
          break;
//          case 4:  -- BUG - didnt parse date
//            try {
//              SimpleDateFormat sdf = new SimpleDateFormat("d.m.yyyy");
//              Date date = sdf.parse(value);
//              record.setSaveDate(new java.sql.Date(date.getTime())); 
//            } catch (Exception e) {}
//          break;
          case 4: 
            if (value.isEmpty()) break; 
            try {
              int count = Integer.valueOf(value);
              record.setDownloadCount(count); 
            } catch (NumberFormatException ne) {}
          break;
          case 5: 
            
            Elements langEls = titleTD.getElementsByTag("img");
            if (!langEls.isEmpty()) {
              String lang = langEls.get(0).attr("alt");
              if (!lang.isEmpty()) {
                record.setLanguage(lang);
              }
            }
          break;
          case 7: 
            if (value.isEmpty()) break; 
            record.setVideoSize(value); 
          break;
          case 8:
            if (value.isEmpty()) break; 
            record.setOriginator(value);
          break;
        }
      }
    }
    return list;
  }
  
  public void clearCache() {
    cache.clear();
  }
  
  private class TitulkyNewThread extends Thread {
    
    private TitulkyNewPage page;
    private int pageNumber = -1;
    private List<TitleListRecord> result;
    
    public TitulkyNewThread(TitulkyNewPage page, int pageNumber, List<TitleListRecord> result) {
      this.page = page;
      this.pageNumber = pageNumber;
      this.result = result;
    }
    
    @Override
    public void run() {
      while (true) {
        try {
          System.out.println("Thread " + this.getId() + " started.");
          long start = System.currentTimeMillis();
          List<TitleListRecord> titles = page.getNewTitles(pageNumber);
          result.addAll(titles);
          long stop = System.currentTimeMillis();
          System.out.println("Thread " + this.getId() + " finished. Time=" + ((stop-start) / 1000.) + "s, titles=" + titles.size());
          break;
        } catch (IOException e) {
          System.out.println("Thread " + this.getId() + " interrupted. Run again.");
          continue;
        }
      }
    }
  }

}
