package com.testapp.test;

import java.util.ArrayList;
import java.util.List;

public class Group {

  public String string;
  public int bizid;
  public final List<String> children = new ArrayList<String>();

  public Group(String string, int bizid) {
    this.string = string;
    this.bizid = bizid;
  }

} 