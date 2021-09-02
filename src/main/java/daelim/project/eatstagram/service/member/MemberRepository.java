package daelim.project.eatstagram.service.member;

import daelim.project.eatstagram.service.member.dsl.MemberDslRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>, MemberDslRepository {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByUsernameAndFormSocial(String username, boolean formSocial);
}
