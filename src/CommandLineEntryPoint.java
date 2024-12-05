
public class CommandLineEntryPoint implements UserRequestProvider<String[]>{

	private String[] args;

	private CommandLineEntryPoint(String[] args) {
		this.args = args;
	}

	public static void main(String[] args) throws Exception {
		if (args[0].equals("--help")) {
			System.out.println("Command line entry point for factorial digit sum program. Takes filename for input and filename to output.");
			System.out.println("Ex: CommandLineEntryPoint inputfilename.txt outputfile.txt");
			System.exit(0);
		}
		if (args.length != 2) {
			System.err.println("Incorrect number of command line arguments, see --help");
			System.exit(1);
		}
		Mediator.getMediator().sendInput(new CommandLineEntryPoint(args));
	}

	@Override
	public UserRequest generateRequest(String[] t) {
		return new UserRequest(new FileUserRequestSource(t[0]), new FileUserRequestDestination(t[1]));
	}

	@Override
	public String[] getInput() {
		return args;
	}

	@Override
	public void propigateResponse(EngineResponse response) {
		if(response == null) {
			System.err.println("Error: Engine responded with null");
		}
		if (response instanceof EngineResponseException engineResponseException) {
			Exception e = engineResponseException.getException();
			e.printStackTrace();
		}
		if (response.getResponseCode().isFailure()){
			System.out.println("Failed :^(");
		} else {
			System.out.println("Successful! Output written at: " + args[1]);
		}
		System.exit(0);
	}
}
