package edu.ualberta.controller;

import java.util.Stack;

import edu.ualberta.controller.CallbackIntefaces.OnCallback;
import edu.ualberta.controller.CallbackIntefaces.OnUndo;
import edu.ualberta.controller.OnUndoListener;

/**
 * <code>CommandManager</code> manages Commands in
 * <code>CommandCollection</code>. This template can be examined in details in
 * 07-Patterns-01.4up.pdf in eclass.
 * 
 * @author Joey Andres
 * @param <CommandAbstract>
 * 
 */
public class CommandManager {
	private Stack<CommandAbstract<OnCallback, OnUndo>> historyList;
	private Stack<CommandAbstract<OnCallback, OnUndo>> redoList;

	private CommandManager() {
		historyList = new Stack<CommandAbstract<OnCallback, OnUndo>>();
		redoList = new Stack<CommandAbstract<OnCallback, OnUndo>>();
	}

	// invoke command aLinkedListednd add it to history list.
	public void invokeCommand(CommandAbstract command) {
		command.execute();
		
		if(command instanceof OnUndoListener) return;	// XXX do not add undo to to the stack.
		if(command instanceof OnRedoListener) return;	// XXX do not add redo to to the stack.
		
		if (command.isReversible()) {
			historyList.add(command);
		} else {
			//historyList.clear();
		}

		if (redoList.empty() == false) {
			redoList.clear();
		}
	}
	
	/**
	 * Pops a command without executing it. This is done when the command is likely
	 * to cause an exception due to unfinnished initializtion.
	 * @return
	 */
	public CommandAbstract popCommand(){
		return historyList.pop();
	}
	
	public void undo(){
		if(historyList.empty() == false){
			CommandAbstract<OnCallback, OnUndo> command = historyList.pop();
			command.unexecute();
			redoList.add(command);
		}
	}
	
	public void redo(){
		if(redoList.empty() == false ){
			CommandAbstract<OnCallback, OnUndo> command = redoList.pop();
			command.execute();
			historyList.add(command);
		}
	}
	
	// CommandManager is a singleton.
	private static final CommandManager instance = new CommandManager();
	public static CommandManager getInstance(){
		return instance;
	}
}
