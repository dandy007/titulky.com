package org.danysoft.lib.web_content_provider.Titulky_com.data;

import java.sql.Date;

public class TitleListRecord {

  private String urlId;
  private String name;
  private String version;
  private String seriesName;
  private int year;
  private Date saveDate;
  private int downloadCount;
  private String language;
  private String videoSize;
  private String originator;
  
  public String getUrlId() {
    return urlId;
  }
  public void setUrlId(String urlId) {
    this.urlId = urlId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  public String getSeriesName() {
    return seriesName;
  }
  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
  }
  public int getYear() {
    return year;
  }
  public void setYear(int year) {
    this.year = year;
  }
  public Date getSaveDate() {
    return saveDate;
  }
  public void setSaveDate(Date saveDate) {
    this.saveDate = saveDate;
  }
  public int getDownloadCount() {
    return downloadCount;
  }
  public void setDownloadCount(int downloadCount) {
    this.downloadCount = downloadCount;
  }
  public String getLanguage() {
    return language;
  }
  public void setLanguage(String language) {
    this.language = language;
  }
  public String getVideoSize() {
    return videoSize;
  }
  public void setVideoSize(String videoSize) {
    this.videoSize = videoSize;
  }
  public String getOriginator() {
    return originator;
  }
  public void setOriginator(String originator) {
    this.originator = originator;
  }
  
  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    
    b.append(urlId + " | ");
    b.append(name + " | ");
    b.append(version + " | ");
    b.append(seriesName + " | ");
    b.append(year + " | ");
    b.append(saveDate + " | ");
    b.append(downloadCount + " | ");
    b.append(language + " | ");
    b.append(videoSize + " | ");
    b.append(originator);
    
    return b.toString();
  }
  
}
