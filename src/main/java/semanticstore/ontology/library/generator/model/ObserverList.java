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

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

@SuppressWarnings("serial")
public abstract class ObserverList extends ArrayList<IObservableItem> implements IObserverList {

  public ObserverList() {
    super();
  }

  @Override
  public abstract void update(IObservableItem item);

  @Override
  public boolean add(IObservableItem e) {
    // TODO Auto-generated method stub
    e.setObserver(this);
    return super.add(e);
  }

  @Override
  public void add(int index, IObservableItem element) {
    // TODO Auto-generated method stub
    element.setObserver(this);
    super.add(index, element);
  }

  @Override
  public IObservableItem remove(int index) {
    // TODO Auto-generated method stub
    super.get(index).setObserver(null);
    return super.remove(index);
  }

  @Override
  public boolean remove(Object o) {
    // TODO Auto-generated method stub
    if (o instanceof IObservableItem) {
      ((IObservableItem) o).setObserver(null);
    }
    return super.remove(o);
  }

  @Override
  public boolean addAll(Collection<? extends IObservableItem> c) {
    // TODO Auto-generated method stub
    c.forEach(action -> {
      action.setObserver(this);
    });
    return super.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends IObservableItem> c) {
    // TODO Auto-generated method stub
    c.forEach(action -> {
      action.setObserver(this);
    });
    return super.addAll(index, c);
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    // TODO Auto-generated method stub
    // super.ran
    super.removeRange(fromIndex, toIndex);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return super.removeAll(c);
  }

  @Override
  public boolean removeIf(Predicate<? super IObservableItem> filter) {
    // TODO Auto-generated method stub
    return super.removeIf(filter);
  }


}
