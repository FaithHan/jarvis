package org.jarvis.tree;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TreeWrapperTest {

    @Test
    void name() {
        ArrayList<Member> members = new ArrayList<>();
        members.add(new Member(1L, -1L, "北京", null));
        members.add(new Member(2L, null, "北京", null));
        members.add(new Member(3L, 1L, "北京", null));
        members.add(new Member(4L, 3L, "北京", null));
        members.add(new Member(5L, 2L, "北京", null));
        members.add(new Member(6L, 2L, "北京", null));
        TreeWrapper<Member> treeWrapper = TreeWrapper.custom().withWeightName("id").build(members);
        System.out.println(treeWrapper.getRootNodeList());
        System.out.println(treeWrapper.getNodeById(5L));
        System.out.println(treeWrapper.getRootNodeByTreeId(5L));
        System.out.println(treeWrapper.toJson());

        Member root = treeWrapper.getNodeById(2L);
        System.out.println(treeWrapper.flatChildren(root));
        System.out.println(treeWrapper.flatTree(root));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Member {
        private Long id;
        private Long parentId;
        private String name;
        private Member[] children;
    }

}