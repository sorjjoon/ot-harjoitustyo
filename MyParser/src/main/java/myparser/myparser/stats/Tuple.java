/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.stats;

/**
 *
 * Just a simple tuple class for usage in Stats
 */
public class Tuple<A,B> {

  private final A First;
  private final B second;

  public Tuple(A first, B second) {
    this.First = first;
    this.second = second;
  }

  public A getFirst() { 
      return First; 
  }
  public B getSecond() { 
      
      return second; 
  }
  
  
  
}
