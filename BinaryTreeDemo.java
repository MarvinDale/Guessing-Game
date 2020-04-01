import java.io.*;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.Stack;

public class BinaryTreeDemo
{
	static String fileName = "treeGame.txt";

	static FileOutputStream file;
	static ObjectOutputStream out;

	public static void main(String[] args)
	{
		// Create a tree
		System.out.println("Constructing a test tree ...");
		BinaryTree<String> testTree = new BinaryTree<String>();
		createTree1(testTree);

		Scanner scanner = new Scanner(System.in);

		//playing the game
		while (true) {
			System.out.println(testTree.getNumberOfNodes());
			//Starting at the root of the tree
			BinaryNodeInterface<String> currentNode = testTree.getRootNode();

			//game keeps running while the tree is being traversed
			//after a leaf node has been reached the traversal is stopped
			while (!currentNode.isLeaf()) {
				//print the question at the current node
				System.out.println(currentNode.getData());

				//creating a scanner that the user uses to input an answer
				String answer = scanner.nextLine();

				//yes answers
				if (answer.equals("yes")) {
					//traversing to the left by setting the current node equal to the left child
					currentNode = currentNode.getLeftChild();

					//if the current node is a leaf the guess method is called
					if (currentNode.isLeaf()) {
						guess(currentNode, scanner);
						displayOptions(scanner, testTree);
					}
				}
				//no answers
				else if (answer.equals("no")) {
					//traversing to the right by setting the current node equal to the right child
					currentNode = currentNode.getRightChild();

					//if the current node is a leaf the guess method is called
					if (currentNode.isLeaf()) {
						System.out.println(currentNode.getData());
						String finalAnswer = scanner.nextLine();

						if (finalAnswer == "no")
						{

						}

						//guess(currentNode, scanner);

						boolean playAgain = false;
						while (!playAgain)
						{
							playAgain = displayOptions(scanner, testTree);
						}
						break;
					}
				}
				//invalid inputs will trigger an error message and the question will be asked again
				else {
					System.out.println("Invalid Input, 'yes' and 'no' are the only valid answers");
				}
			}
		}
	} // end of main


	public static void createTree1(BinaryTree<String> tree)
	{
		// To create a tree, build it up from the bottom:
		// create subtree for each leaf, then create subtrees linking them,
		// until we reach the root.

		//level 3
		BinaryTree<String> h = new BinaryTree<String>("Is it a Dog?");
		BinaryTree<String> i = new BinaryTree<String>("Is it a Whale?");

		BinaryTree<String> j = new BinaryTree<String>("Is it a Crow?");
		BinaryTree<String> k = new BinaryTree<String>("Is it a Penguin?");

		BinaryTree<String> l = new BinaryTree<String>("Is it Tom Hanks?");
		BinaryTree<String> m = new BinaryTree<String>("Is it Donald Trump?");

		BinaryTree<String> n = new BinaryTree<String>("Is it Ireland?");
		BinaryTree<String> o = new BinaryTree<String>("Is it an iPhone?");

		// Now the subtrees joining leaves, level 2
		BinaryTree<String> d = new BinaryTree<String>("Is it a land animal?", h, i);
		BinaryTree<String> e = new BinaryTree<String>("Can it fly?", j, k);

		BinaryTree<String> f = new BinaryTree<String>("Are they an actor?", l, m);
		BinaryTree<String> g = new BinaryTree<String>("Are you thinking of a place?", n, o);

		// Now the subtrees joining leaves, level 1
		BinaryTree<String> b = new BinaryTree<String>("Is it a mammal?", d, e);
		BinaryTree<String> c = new BinaryTree<String>("Are you thinking of a person?", f, g);

		// Now the root
		tree.setTree("Are you thinking of an animal?", b, c);
	} // end createTree1

	public static void guess(BinaryNodeInterface<String> currentNode, Scanner scanner) {
		//prints out the game's guess
		System.out.println(currentNode.getData());
		String guess = scanner.nextLine();

		//if the guess is correct
		if (guess.equals("yes"))
		{
			System.out.println("The tree guessed correctly!!\n");
		}
		else if (guess.equals("no"))
		{
			System.out.println("I don't know what is the correct answer?");
			BinaryNode correctAnswer = new BinaryNode(scanner.nextLine());

			System.out.println("Enter a distinguishing question to expand the tree");
			String newQuestion = scanner.nextLine();

			BinaryNode no = new BinaryNode(currentNode.getData());
			currentNode.setData(newQuestion);
			currentNode.setLeftChild(no);
			currentNode.setRightChild(correctAnswer);
		}
	}

	private static boolean displayOptions(Scanner scanner, BinaryTree<String> testTree) {
		System.out.println("Press '1' to play again");
		System.out.println("Press '2' to quit");
		System.out.println("Press '3' to store the current tree");
		System.out.println("Press '4' press 4 to load a stored tree");

		String chooseOption = scanner.nextLine();

		switch (chooseOption) {
			case "1":
				return true;
			case "2":
				System.exit(0);
			case "3":
				saveTree(testTree);
				break;
			case "4":
				testTree = loadTree();
		}
		return false;
	}

	public static void saveTree(BinaryTree<String> testTree)
	{

		Stack<BinaryNode> stack = new Stack<BinaryNode>();
		stack.push((BinaryNode) testTree.getRootNode());
		StringBuilder sb = new StringBuilder();

		while (!stack.isEmpty())
		{
			BinaryNode h = stack.pop();
			if (h != null) {
				sb.append(h.getData() + ",");
				stack.push((BinaryNode) h.getRightChild());
				stack.push((BinaryNode) h.getLeftChild());
			} else {
				sb.append("#,");
			}
		}

			//Object treeData = sb.toString().substring(0, sb.length() - 1);

			try
			{
				file = new FileOutputStream(fileName);
				out = new ObjectOutputStream(file);

				out.writeObject(sb.toString().substring(0, sb.length() - 1));
				out.close();
				file.close();

				System.out.println("Save Complete\n Data Stored: ");
			}
			catch (IOException ex)
			{
				System.out.println("IOException caught");
			}

	}

	public static BinaryTree<String> loadTree ()
	{
		try
		{
			// Reading the object from a file
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			String serializedData = (String)in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized ");
			System.out.println("String loaded = " + serializedData);

			BinaryTree<String> loadedData = deserialize(serializedData);

			return loadedData;

		}

		catch (IOException ex)
		{
			System.out.println("IOException is caught");
		}

		catch(ClassNotFoundException ex)
		{

			System.out.println("ClassNotFoundException is caught");
		}
		return null;
	}

	// Decodes your encoded data to tree.
	public static BinaryTree<String> deserialize(String data)
	{
		if(data == null)
			return null;

		int[] t = {0};
		String[] arr = data.split(",");

		return helper(arr, t);
	}

	public static BinaryTree<String> helper(String[] arr, int[] t)
	{
		if(arr[t[0]].equals("#"))
		{
			return null;
		}

		BinaryTree<String> root = new BinaryTree<String>(arr[t[0]], helper(arr, t), helper(arr, t));
		//System.out.println(root.getData());

//		t[0]=t[0]+1;
//		root.setLeftChild(helper(arr, t));
//		t[0]=t[0]+1;
//		root.setRightChild(helper(arr, t));
//
//		System.out.println(root.getData() + "root data");

		return root;
	}
}