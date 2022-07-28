package org.jarvis.security.jwt;

import com.auth0.jwt.interfaces.Claim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <b>各类JWT库(java)的使用与评价</b><br>
 * <p>//https://andaily.com/blog/?p=956</p>
 */
public class JWTUtils {

    private static JWTService jwtService = new HS256JWTService();

    public static void register(JWTService jwtService) {
        JWTUtils.jwtService = jwtService;
    }

    public static String createToken(Object userInfo) {
        return jwtService.createToken(userInfo);
    }

    public static Map<String, Claim> verifyToken(String token) {
        return jwtService.verifyToken(token);
    }

    public static <T> T getUserInfo(String token, Class<T> classValue) {
        return jwtService.getUserInfo(token, classValue);
    }


    static class Solution {

        static class TreeNode {
            TreeNode left;
            TreeNode right;
            int val;
        }

        private Set<Integer> indexList = new HashSet<>();


        public List<List<Integer>> hehe(int[] array, List<Integer> element, List<List<Integer>> result) {
            if (element.size() == array.length) {
                result.add(element);
                return result;
            }
            for (int i = 0; i < array.length; i++) {
                if (!indexList.contains(i)) {
                    indexList.add(i);
                    ArrayList<Integer> integers = new ArrayList<>(element);
                    integers.add(array[i]);
                    hehe(array, integers, result);
                    indexList.remove(i);
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        List<List<Integer>> hehe =
                new Solution().hehe(new int[]{1, 2, 3,4}, new ArrayList<>(), new ArrayList<>());
        System.out.println(hehe);
        System.out.println(hehe.size());
    }
}
