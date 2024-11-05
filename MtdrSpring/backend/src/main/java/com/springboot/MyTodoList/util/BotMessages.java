package com.springboot.MyTodoList.util;

public enum BotMessages {
	
	HELLO_MYTODO_BOT(
	"Hello! I'm Chocobot!\nType a new to do ChocoItem below and press the send button (blue arrow), or select an option below:"),
	BOT_REGISTERED_STARTED("Bot registered and started succesfully!"),
	ITEM_DONE("ChocoItem done! Select /todolist to return to the list of todo ChocoItems, or /start to go to the main screen."), 
	ITEM_UNDONE("ChocoItem undone! Select /todolist to return to the list of todo ChocoItems, or /start to go to the main screen."), 
	ITEM_DELETED("ChocoItem deleted! Select /todolist to return to the list of todo ChocoItems, or /start to go to the main screen."),
	TYPE_NEW_TODO_ITEM("Type a new todo ChocoItem below and press the send button (blue arrow) on the right-hand side."),
	NEW_ITEM_ADDED("New ChocoItem added! Select /todolist to return to the list of todo ChocoItems, or /start to go to the main screen."),
	GRAPHICS("Graphics command"), 
	ITEM_EDIT("Edit Choco-task"), 
	STORYPOINTS("How many Choco-storypoints?"),
	RESPONSABLE("Which Choco-partner is assigned?"),
	BYE("Bye! Select /start to resume!");

	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
