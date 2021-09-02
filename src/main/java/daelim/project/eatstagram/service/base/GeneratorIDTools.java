package daelim.project.eatstagram.service.base;

import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class GeneratorIDTools {

    private static AtomicInteger index = new AtomicInteger();

    public static int nextVal() {
        if (index.get() > 99999) {
            index.set(0);
        }
        return index.getAndIncrement();
    }

    /**
     * 유니크한ID를 생성 해주는 기능
     *
     * @param prefix
     * @return {@link String} 유니크ID
     */
    public static String getId(String prefix) {
        return EIDType.NORMAL.getId(prefix);
    }

    public static String getId(String prefix, EIDType idType) {
        return idType.getId(prefix);
    }
}
