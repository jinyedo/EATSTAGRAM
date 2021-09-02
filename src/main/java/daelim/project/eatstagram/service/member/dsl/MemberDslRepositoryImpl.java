package daelim.project.eatstagram.service.member.dsl;

import daelim.project.eatstagram.service.member.QMember;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberDslRepositoryImpl extends QuerydslRepositorySupport implements MemberDslRepository {

    public MemberDslRepositoryImpl() {
        super(QMember.class);
    }
}
