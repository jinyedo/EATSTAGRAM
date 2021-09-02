package daelim.project.eatstagram.service.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 테이블 유니크 ID 생성 기능 제공
 * @author jinyedo
 */

public enum EIDType {

    LONG {
        @Override
        public String getId(String prefix) {
            return String.format("%s-%s-%05d",prefix,
                    (new SimpleDateFormat("yyMMddHHmmssSSS")).format(new Date()), GeneratorIDTools.nextVal());
        }
    },
    NORMAL {
        @Override
        public String getId(String prefix) {
            return String.format("%s-%s-%05d", prefix, System.currentTimeMillis(), GeneratorIDTools.nextVal());
        }
    },
    SHORT {
        @Override
        public String getId(String prefix) {
            return String.format("%s%s%03d", prefix, (new SimpleDateFormat("yyMMddHHmm")).format(new Date()), GeneratorIDTools.nextVal());
        }
    };

    public abstract String getId(String prefix);
}
