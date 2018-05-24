class BuiltInList {
	static void hello() {
		System.out.println("Hello, World!");
	}

	static void help() {
		System.out.println("Shell 0.1b ");
        System.out.println("Built-ins: ");
        System.out.println("hello - print 'Hello World'");
		System.out.println("exit - exits Shell");
        System.out.println("help - prints this text");
		System.out.println("getdir - prints current directory [DEBUG MODE]");
		System.out.println("! - re-run last command [NON WORKING]");
	}

	static void exit(int status) {
		System.exit(status);
	}

	static void getdir(boolean debug) {
		if (debug) {
			System.out.println("hello");
		}
	}

	/* case("getdir"):
          debug(currentDir);
          if(isDebug()) {
            System.out.println("Enable Debug mode to run this command");
          }
        break; */
		
    /*  case("!"):
	    System.out.println(lastCmd);
	try {
	  externRun(splitLine(lastCmd));
        } catch (NullPointerException e) {
          System.out.println("You haven't run any Commands yet.");
	  break;
        }
        break;
	  case "echo":
	    System.out.println(
      case "meow":
        System.out.println("Meow.");
		break;
	  case "cd":
	    StringBuilder completePath = new StringBuilder(); //strings
		completePath.append(String.join("\\", currentFolder));
		debug("Complete String: " + completePath.toString());
		if (command.size() == 1) {
			System.out.println("cd: Help me.");
			break;
		} else if (command.get(0).equals("..")) {
				currentFolder.remove(currentFolder.size());
				debug("cd: " + currentFolder.toString());
				break;
		}	/* else {
			//command.get(1) works here
			Path path = Paths.get(completePath.toString() + "\\" + command.get(1));
			debug(completePath.toString() + "\\" + command.get(1));
			if (Files.exists(path)) {
				currentFolder.add(command.get(1));
				debug(currentFolder.toString());
			}
			/* folder.add(completePath.toString());
			//folder.add(
			debug(completePath.toString() + "\\" + command.get(1)); 
			break;
		if (command.get(1).equals("..")) {
			debug("Changing directory upwards...");
		} else if (command.size() == 1 || command.get(1).equals("-h")) {
			debug("Help: cd [DIRECTORY]");
		} else {
			if (Files.exists(Paths.get(completePath.toString()))) {
			  //folder.append(command.get(1));
			  debug("Changing into " + command.get(1));
			  break;
			} else {
				debug(command.get(1) + ": not found");
			} 
      default:
        externRun(command);
        break; */
}