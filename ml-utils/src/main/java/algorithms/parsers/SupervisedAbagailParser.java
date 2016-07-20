package algorithms.parsers;

import org.jetbrains.annotations.NotNull;
import shared.Instance;

public class SupervisedAbagailParser extends AbagailParser {
    @NotNull
    @Override
    protected Instance parseAbagailInstance(datasets.Instance myInstance) {
        shared.Instance abagailInstance = super.parseAbagailInstance(myInstance);

        abagailInstance.setLabel(new Instance(myInstance.getOutput()));

        return abagailInstance;
    }
}
