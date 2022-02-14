public class ReverseString {
    public static void main(String[] args){
        System.out.println(reverse("Matthew"));
    }

    public static String reverse(String s){
        String result = "";
        for(int i = 0; i<s.length(); i++){
            result = s.charAt(i) + result;
        }

        return result;
    }
}
