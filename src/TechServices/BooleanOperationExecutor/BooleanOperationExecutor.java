/*********************************************************************
	Rhapsody	: 8.0.3
	Login		: dxc122030
	Component	: DefaultComponent
	Configuration 	: DefaultConfig
	Model Element	: BooleanOperationExecutor
//!	Generated Date	: Mon, 11, Apr 2016 
	File Path	: DefaultComponent/DefaultConfig/TechServices/BooleanOperationExecutor/BooleanOperationExecutor.java
*********************************************************************/

package TechServices.BooleanOperationExecutor;

import java.util.ArrayList;
import java.util.Stack;





//## operation getFilteredPeriod(WFAPeriod,String) 
import Business.Model.WFAPeriod;

//----------------------------------------------------------------------------
// TechServices/BooleanOperationExecutor/BooleanOperationExecutor.java                                                                  
//----------------------------------------------------------------------------

//## package TechServices::BooleanOperationExecutor 


//## class BooleanOperationExecutor 
public class BooleanOperationExecutor {
    
	//defined constants for regex values
    public static final String OPERATION_REGEX = "&&&";
    public static final String OPERAND_REGEX = "::";
    public static final String STATEMENT_REGEX = ":";
    
    //defined constants for boolean operations and constants
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String AND = "and";
    public static final String OR = "or";
    
    //defined constants for statement operations
    public static final String EQUAL_TO = "==";
    public static final String NOT_EQUAL_TO = "!=";
    public static final String GREATER_THAN = ">";
    public static final String LESS_THAN = "<";
    public static final String GREATER_THAN_OR_EQUAL_TO = ">=";
    public static final String LESS_THAN_OR_EQUAL_TO = "<=";
    
    // Constructors
    
    //## auto_generated 
    public  BooleanOperationExecutor() {
    }
    
    //AND
    private String and(String operand1, String operand2) {
    	return operand1.equals(TRUE) && operand2.equals(TRUE) ? TRUE : FALSE;
    }
    
    //OR
    private String or(String operand1, String operand2) {
    	return operand1.equals(TRUE) || operand2.equals(TRUE) ? TRUE : FALSE;
    }
    
    //EQUAL_TO
    private String equalTo(String operand1, String operand2) {
    	return operand1.equals(operand2) ? TRUE : FALSE;
    }

    //NOT_EQUAL_TO
    private String notEqualTo(String operand1, String operand2) {
    	return !operand1.equals(operand2) ? TRUE : FALSE;
    }

    //GREATER_THAN
    private String greaterThan(String operand1, String operand2) {
    	return Double.valueOf(operand1) > Double.valueOf(operand2) ? TRUE : FALSE;
    }

    //LESS_THAN
    private String lessThan(String operand1, String operand2) {
    	return Double.valueOf(operand1) < Double.valueOf(operand2) ? TRUE : FALSE;
    }

    //GREATER_THAN_OR_EQUAL_TO
    private String greaterThanOrEqualTo(String operand1, String operand2) {
    	return Double.valueOf(operand1) >= Double.valueOf(operand2) ? TRUE : FALSE;
    }

    //LESS_THAN_OR_EQUAL_TO
    private String lessThanOrEqualTo(String operand1, String operand2) {
    	return Double.valueOf(operand1) <= Double.valueOf(operand2) ? TRUE : FALSE;
    }
    
    //evaluates a single statement without any boolean operations to "true" or "false"
    private String evaluateStatement(ArrayList<String> entries, ArrayList<String> columns, 
    		String statement) {
    	String evaluation = null;  //true/false evaluation of this statement
    	String[] tokens = statement.split(STATEMENT_REGEX);

    	//we should always have three tokens as follows
    	String operation = tokens[1];
    	String operand1 = tokens[0];
    	String operand2 = tokens[2];
    	
    	//need to find the matching value for operand1
    	System.out.print(operand1 + ", " + columns.indexOf(operand1) 
    			+ ", " + entries.size() + ": ");
    	
    	operand1 = entries.get(columns.indexOf(operand1));
    	System.out.println(operand1);
    	
    	//find matching statement operation
    	//note: the following is a possible candidate for polymorphism
    	if(operation.equals(EQUAL_TO)) {
    		evaluation = equalTo(operand1, operand2);
    	}
    	else if(operation.equals(NOT_EQUAL_TO)) {
    		evaluation = notEqualTo(operand1, operand2);
    	}
    	else if(operation.equals(GREATER_THAN)) {
    		evaluation = greaterThan(operand1, operand2);
    	}
    	else if(operation.equals(LESS_THAN)) {
    		evaluation = lessThan(operand1, operand2);
    	}
    	else if(operation.equals(GREATER_THAN_OR_EQUAL_TO)) {
    		evaluation = greaterThanOrEqualTo(operand1, operand2);
    	}
    	else if(operation.equals(LESS_THAN_OR_EQUAL_TO)) {
    		evaluation = lessThanOrEqualTo(operand1, operand2);
    	}
    	else {
    		//should never execute, print error message indicating so
    		System.out.println("Error, unexpected statement operation: " + operation);
    	}
    	
    	//return evaluation of this statement
    	return evaluation;
    }
    
    //evaluates a single boolean operation, evaluates operand statements if not "true" or "false"
    private String evaluateBooleanOperation(ArrayList<String> entries, ArrayList<String> columns, 
    		String operation, String operand1, String operand2) {
    	String evaluation = null;  //true/false evaluation of this operation
    	
    	//evaluate operand statements 1 & 2 if it they have not already been evaluated
    	if(!operand1.equals(TRUE) && !operand1.equals(FALSE)) {
    		operand1 = evaluateStatement(entries, columns, operand1);
    	}
    	if(!operand2.equals(TRUE) && !operand2.equals(FALSE)) {
    		operand2 = evaluateStatement(entries, columns, operand2);
    	}
    	
    	//perform the indicated logical operation
    	if(operation.equals(AND)){
    		evaluation = and(operand1, operand2);
    	}
    	else if(operation.equals(OR)) {
    		evaluation = or(operand1, operand2);
    	}
    	else {
    		//should never execute, print error message indicating so
    		System.out.println("Error, unexpected logical operation: " + operation);
    	}
    	
    	//return evaluation of this operation
    	return evaluation;
    }
    
    //evaluates a single operand to true/false
    private String evaluateGroup(ArrayList<String> entries, ArrayList<String> columns, 
    		String operand) {
    	//create stacks to evaluate the operand with
    	Stack<String> operands = new Stack<String>();
		Stack<String> operations = new Stack<String>();
		
		//split operations using regex
    	String[] tokens = operand.split(OPERAND_REGEX);
    	
    	//check for degenerate case of one statement/token
    	if(tokens.length == 1) {
    		return evaluateStatement(entries, columns, tokens[0]); 
    	}
    	else {
    		//put operands and operators into their respective stacks
        	//note: we add them in reverse to preserve stack order
        	for(int i = tokens.length - 1; i >= 0; i = i - 2) { 
        		operands.push(tokens[i]);
        	}
        	for(int i = tokens.length - 2; i >= 0; i = i - 2) { 
        		operations.push(tokens[i]);
        	}
        	
        	//execute operations until the stack is empty
        	while(!operations.isEmpty()) {
        		String operation = operations.pop();
        		String operand1 = operands.pop();
        		String operand2 = operands.pop();
        		operands.push(evaluateBooleanOperation(entries, columns,
        				operation, operand1, operand2));
        	}

        	//return evaluation of this operand group
        	return operands.pop();
    	}
    }
    
    //evaluates a single boolean operation, evaluates operand groups if not "true" or "false"
    private String evaluateBooleanOperations(ArrayList<String> entries, ArrayList<String> columns, 
    		String operation, String operand1, String operand2) {
    	String evaluation = null;  //true/false evaluation of this operation
    	
    	//evaluate operand groups 1 & 2 if it they have not already been evaluated
    	if(!operand1.equals(TRUE) && !operand1.equals(FALSE)) {
    		operand1 = evaluateGroup(entries, columns, operand1);
    	}
    	if(!operand2.equals(TRUE) && !operand2.equals(FALSE)) {
    		operand2 = evaluateGroup(entries, columns, operand2);
    	}
    	
    	//perform the indicated logical operation
    	if(operation.equals(AND)){
    		evaluation = and(operand1, operand2);
    	}
    	else if(operation.equals(OR)) {
    		evaluation = or(operand1, operand2);
    	}
    	else {
    		//should never execute, print error message indicating so
    		System.out.println("Error, unexpected logical operation: " + operation);
    	}
    	
    	//return evaluation of this operation
    	return evaluation;
    }

    //evaluates a string operation to true or false
    private Boolean passesFilter(ArrayList<String> entries, ArrayList<String> columns, 
    		String input) {
    	//check if string is empty
    	if (input != null && !input.isEmpty()) {
    		Stack<String> operands = new Stack<String>();
    		Stack<String> operations = new Stack<String>();
    		
    		//split operations using regex
        	String[] tokens = input.split(OPERATION_REGEX);
        	
        	//check for degenerate case of one statement/token
        	if(tokens.length == 1) {
        		return evaluateGroup(entries, columns, tokens[0]).equals(TRUE) ? true : false; 
        	}
        	else {
            	//put operands and operators into their respective stacks
            	//note: we add them in reverse to preserve stack order
            	for(int i = tokens.length - 1; i >= 0; i = i - 2) { 
            		operands.push(tokens[i]);
            	}
            	for(int i = tokens.length - 2; i >= 0; i = i - 2) { 
            		operations.push(tokens[i]);
            	}
            	
            	//evaluate boolean operations until the stack is empty
            	while(!operations.isEmpty()) {
            		String operation = operations.pop();
            		String operand1 = operands.pop();
            		String operand2 = operands.pop();
            		operands.push(evaluateBooleanOperations(entries, columns,
            				operation, operand1, operand2));
            	}
            	
            	return operands.pop().equals(TRUE) ? true : false;
        	}
    	}
    	else {
    		//no filtering operations, thus return true
    		return true;
    	}
    }

    /**
     * This method filters a WFAPeriod given a sting containing a filter
     * It accepts any number of logical operations and statements including
     * zero. 
     * 
     * Sample filter: "Headcount:==:0&&&and&&&State:==:E::or::State:==:G"
     * 
     * Where:
     * "&&&" is the public OPERATION_REGEX 
     * "::" is the OPERAND_REGEX
     * ":" is the STATEMENT_REGEX
     * 
     * @author Daren Cheng
     * @param period the period to be filtered
     * @param filter a string containing the filter for this period. This 
     * filter must use the regex string constants defined by class
     * @return WFAPeriod that has been filtered by the given filter
    */
    public WFAPeriod getFilteredPeriod(WFAPeriod period, String filter) {
    	WFAPeriod filteredPeriod = new WFAPeriod();  //filtered period to return
    	
    	//copy over columns
    	filteredPeriod.getStart().setColumns(period.getStart().getColumns());
    	filteredPeriod.getEnd().setColumns(period.getEnd().getColumns());
    	filteredPeriod.getActivity().setColumns(period.getActivity().getColumns());
    	
    	//add start entries that pass the filter
    	for(ArrayList<String> entry : period.getStart().getEntries()) {
    		if (passesFilter(entry, period.getStart().getColumns(), filter)){
    			filteredPeriod.getStart().getEntries().add(entry);
    		}
    	}
    	
    	//add end entries that pass the filter
    	for(ArrayList<String> entry : period.getEnd().getEntries()) {
    		if (passesFilter(entry, period.getEnd().getColumns(), filter)){
    			filteredPeriod.getEnd().getEntries().add(entry);
    		}
    	}

    	//add start entries that pass the filter
    	for(ArrayList<String> entry : period.getActivity().getEntries()) {
    		if (passesFilter(entry, period.getActivity().getColumns(), filter)){
    			filteredPeriod.getActivity().getEntries().add(entry);
    		}
    	}
    	
    	//return filtered period
    	return filteredPeriod;
    }
    
}
/*********************************************************************
	File Path	: DefaultComponent/DefaultConfig/TechServices/BooleanOperationExecutor/BooleanOperationExecutor.java
*********************************************************************/

