/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.model;

import java.io.Serializable;

public class ZPair<K, V> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private K key;
  private V value;

  public ZPair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public void setKey(K key) {
    this.key = key;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ZPair<?, ?> other = (ZPair<?, ?>) obj;


    if (key == null) {
      if (other.key != null || other.value != null)
        return false;
    } else if (value == null) {
      if (other.key != null || other.value != null)
        return false;
    } else if (!((key.equals(other.key) && value.equals(other.value))
        || (key.equals(other.value) && value.equals(other.key))))
      return false;

    return true;
  }

  public ZPair<V, K> getReverse() {
    return new ZPair<V, K>(this.value, this.key);
  }

}
