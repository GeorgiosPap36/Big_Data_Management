import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

public class ComputeMathExpression {

    public String Compute(String mathExp){
        try{
            return CalcRPN(computeMathExp(mathExp));
        } catch (Exception e){
            return "Math expression format is incorrect";
        }
        
    }

    private String CalcRPN(Queue<String> exp) throws Exception{
        Stack<Float> finalStack = new Stack<>();

        while(exp.size() > 0){
            String nextS = exp.remove();
            //System.out.println(nextS);
            if (!isFunctionStart(nextS) && !isOperator(nextS)){
                Float number = Float.parseFloat(nextS);
                //System.out.println(number);
                finalStack.add(number);
            }
            else if (isOperator(nextS)){
                float number1 = finalStack.pop();
                float number2 = finalStack.pop();
                System.out.println(number1);
                System.out.println(number2);
                if (nextS.equals("+")){
                    finalStack.add(number2 + number1);
                }
                else if (nextS.equals("-")){
                    finalStack.add(number2 - number1);
                }
                else if (nextS.equals("*")){
                    finalStack.add(number2 * number1);
                }
                else if (nextS.equals("/")){
                    finalStack.add(number2 / number1);
                }
                else if (nextS.equals("^")){
                    finalStack.add((float)(Math.pow(number2, number1)));
                }
            }
            else if (isFunctionStart(nextS)){
                float number = finalStack.pop();
                //System.out.println(number);
                if (nextS.equals("sin")){
                    finalStack.add((float)Math.sin(number));
                }
                else if (nextS.equals("cos")){
                    finalStack.add((float)Math.cos(number));
                }
                else if (nextS.equals("tan")){
                    finalStack.add((float)Math.tan(number));
                }
                else if (nextS.equals("log")){
                    finalStack.add((float)Math.log10(number));
                }
            }
            System.out.println(finalStack);
        }
        if (finalStack.size() == 1){
            String answer = finalStack.pop().toString();
            return answer;
        }
        

        return "Math expression format is incorrect";
    }

    private Queue<String> computeMathExp(String mathExp){

        Queue<String> expQ = new LinkedList<String>();
        Queue<String> outputQ = new LinkedList<String>();
        Stack<String> opStack = new Stack<>();

        for (int i = 0; i < mathExp.length(); i++){
            expQ.add(Character.toString(mathExp.charAt(i)));
        }

        String token = "";
        
        while(expQ.size() > 0){

            if (!isOperator(expQ.peek()) && !isParenthesis(expQ.peek()) && !isFunctionStart(expQ.peek())){
                token += expQ.remove();
                String nextStr = expQ.peek();
                while(expQ.size() > 0 && !isOperator(nextStr) && !isParenthesis(nextStr) && !isFunctionStart(nextStr))
                {
                    token += expQ.remove();
                    nextStr = expQ.peek();
                }
                System.out.println("number token = " + token);
                //System.out.println(expQ);
                outputQ.add(token);
            }
            else if (isFunctionStart(expQ.peek())){
                token += expQ.remove();
                String nextStr = expQ.peek();
                while(!isOperator(nextStr) && !isParenthesis(nextStr))
                {
                    token += expQ.remove();
                    nextStr = expQ.peek();
                }
                System.out.println("function token = " + token);
                opStack.add(token);
            }
            else if (isOperator(expQ.peek())){
                while (opStack.size() > 0 && isOperator(opStack.peek()) && !opStack.peek().equals("(")){
                    if ((opPrecedence(opStack.peek()) > opPrecedence(expQ.peek()) || (opPrecedence(opStack.peek()) == opPrecedence(expQ.peek()) && isLeftAssosiative(expQ.peek())))){
                        outputQ.add(opStack.pop());
                    }
                    else{
                        break;
                    }
                }
                
                System.out.println("operator = " + expQ.peek());
                opStack.add(expQ.remove());
            }
            else if (expQ.peek().equals("(")){
                System.out.println("left parenthesis = " + expQ.peek());
                opStack.add(expQ.remove());
            }
            else if (expQ.peek().equals(")")){
                System.out.println("right parenthesis = " + expQ.remove());
                while (opStack.size() > 0 && !opStack.peek().equals("(")){
                    outputQ.add(opStack.pop());
                }
                if (opStack.size() > 0 && opStack.peek().equals("("))
                    opStack.pop();

                if (opStack.size() > 0 && isFunctionStart(opStack.peek())){
                    token += opStack.pop();
                    while(opStack.size() > 0 && !isOperator(opStack.peek()) && !isParenthesis(opStack.peek()))
                    {
                        token += opStack.pop();
                    }
                    //System.out.println("function token in opStack = " + token);
                    outputQ.add(token);
                }
                //System.out.println("right parenthesis = " + expQ.peek());
            }
            token = "";
        }

        while(opStack.size() > 0){
            if (opStack.peek().equals("(")){
                opStack.pop();
            }
            else{
                outputQ.add(opStack.pop());
            }
        }

        //while(outputQ.size() > 0){
        //    System.out.print(outputQ.remove() + " ");
        //}

        return outputQ;

    }

    private boolean isOperator(String s){
        if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^")){
            return true;
        }
        return false;
    }

    private int opPrecedence(String s){
        if (s.equals("^"))
            return 4;
        else if (s.equals("*") || s.equals("/"))
            return 3;
        else if (s.equals("+") || s.equals("-"))
            return 2;
        return 0;
    }

    private boolean isLeftAssosiative(String s){
        if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/"))
            return true;
        else if (s.equals("^"))
            return false;

        return false;
    }

    private boolean isFunctionStart(String s){
        if ("sin".startsWith(s) || "log".startsWith(s) || "tan".startsWith(s) || "cos".startsWith(s))
            return true;
        return false;
    }

    private boolean isParenthesis(String s){
        if (s.equals("(") || s.equals(")"))
            return true;
        return false;
    }

}
