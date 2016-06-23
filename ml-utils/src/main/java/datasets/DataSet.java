package datasets;

import java.util.*;

public class DataSet<T extends Instance> implements Iterable<T> {
    private T[] instances;

    public DataSet(T[] instances) {
        this.instances = instances;
    }

    public Instance[] getInstances() {
        return instances;
    }

    public List[] splitDataSetRandomly(int ways) {
        List[] divisions = new List[ways];

        for (int i = 0; i < divisions.length; i++) {
            divisions[i] = new ArrayList<T>();
        }

        List<T> instanceList = new ArrayList<>(Arrays.asList(instances));

        for (int i = 0; i < instances.length; i++) {
            divisions[i % divisions.length].add(instanceList.remove((int)(Math.random() * instanceList.size())));
        }

        return divisions;
    }

    @Override
    public Iterator<T> iterator() {
        return new DataSetIterator();
    }

    private class DataSetIterator implements Iterator<T> {

        private int index;

        @Override
        public boolean hasNext() {
            return index < instances.length;
        }

        @Override
        public T next() {
            return (T) instances[index++];
        }
    }
}
