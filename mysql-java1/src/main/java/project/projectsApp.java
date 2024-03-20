package project; 

	import java.math.BigDecimal;
	import java.util.List;
	import java.util.Objects;
	import java.util.Scanner;

	import project.dao.DbConnection;
	import project.dao.DbException;
import project.entity.Project;
import project.exception.ProjectService;

	public class projectsApp {
		private Scanner scanner = new Scanner(System.in);
		private ProjectService projectService = new ProjectService();
		private Project curProject;		
		
		private List<String> operations = List.of( 
				"1) Add a project",
				"2) List projects",
				"3) Select a project",
				"4) Update project details",
				"5) Delete a project"
		); 
		public static void main(String[] args) {
			new projectsApp().processUserSelections();
		}
		
		private void processUserSelections() {
			// TODO Auto-generated method stub
			boolean done = false;
			while (!done) {
				try {
					int selection = getUserSelection1();
					
					switch(selection) {
					case -1: done = exitMenu();
					break;
					
					case 1: createProject();
					break;
					
					case 2:
						listProjects();
						break;
						
					case 3:
						selectProjects();
						break;
						
					case 4:
						updateProjectDetails();
						break;
						
					case 5:
						deleteProject();
						break;
					
					default:
						System.out.println("/n +" + selection + " is not a valid selection. Try again.");
					break;
					}
				}
				catch (Exception e) {
					System.out.println("/nError; " + e + "Try again.");
				}
			}
		}

		private void deleteProject() {
			// TODO Auto-generated method stub
			listProjects();
			 Integer projectId = getIntInput(" Enter the ID of the project to delete");
			 
			 projectService.deleteProject(projectId);
			 System.out.println("Project " + projectId + " was deleted successfuly.");
			 
			 if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
				 curProject = null;
			 }
		}

		private void updateProjectDetails() {
			// TODO Auto-generated method stub
			 if(Objects.isNull(curProject)) {
				 System.out.println("\nPlease select a project.");
				 return;
			 }
		String projectName = 
				getStringInput1("Enter the project name [" + curProject.getProjectName() + "]");
		
		BigDecimal estimatedHours =
				getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours()+ "]");
		
		BigDecimal actualHours =
				getDecimalInput("Enter the actual hours [" + curProject.getActualHours()+ "]");
		
		Integer difficulty = 
				getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		
		String notes = 
				getStringInput1("Enter the project notes [" + curProject.getNotes() + "]");
		
		Project project =
				new Project();
		
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());
		
		}
		private void listProjects() {
			// TODO Auto-generated method stub
			List<project.entity.Project> projects = projectService.fetchAllProjects();
			
			System.out.println("\nProjects: ");
			
			projects.forEach(project -> System.out.println("  " + project.getProjectId()+ " : " + project.getProjectName()));
		}

		private void selectProjects() {
			// TODO Auto-generated method stub
			listProjects();
			Integer projectId =  getIntInput("Enter a project ID to select a project");
			
			curProject = null;
			
			curProject = projectService.fetchProjectById(projectId);
		}

		private  void createProject() {
			// TODO Auto-generated method stub
			String projectName= getStringInput1("Enter the project name");
			BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
			BigDecimal actualHours = getDecimalInput("Enter the actual hours");
			Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
			String notes = getStringInput1("Enter the project notes");
			
			Project project = new Project();
			
			project.setProjectName(projectName);
			project.setEstimatedHours(estimatedHours);
			project.setActualHours(actualHours);
			project.setDifficulty(difficulty);
			project.setNotes(notes);
			
			Project dbProject = projectService.addProject(project);
			System.out.println("You have successfullt created project: " + dbProject);	
		}

		private BigDecimal getDecimalInput(String prompt) {
			// TODO Auto-generated method stub
			String input = getStringInput1(prompt);
			if(Objects.isNull(input)) {
				return null;
			}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + "is not a valid decimal number.");
		}
	}

		private boolean exitMenu() {
			System.out.println("Exiting the menu.");
			return true;
		}
		private int getUserSelection1() {
		printoperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1: input;
	}
		
		

		private Integer getIntInput(String prompt) {
			// TODO Auto-generated method stub
			String input = getStringInput1(prompt);
			if (Objects.isNull(input)) {
					return null;
			}
				try {
					return Integer.valueOf(input);
				}
				catch(NumberFormatException e) {
					throw new DbException(input + " is not a valid number.");
				}
		}
		
		private String getStringInput1(String prompt) {
			System.out.print(prompt + " : ");
			String input = scanner.nextLine();
			
			return input.isBlank() ? null : input.trim();	
		}
		
		
		private void printoperations() {
		// TODO Auto-generated method stub
		System.out.println("/n These are the avaliable selections. Press the ENTER KEY to quit.");
		operations.forEach(line -> System.out.println("  " + line));
		
		
		if (Objects.isNull(curProject)) {
			System.out.println("\\nYou are not working with a project.");
		} else {
		System.out.println("\nYou are working with a project:  " + curProject);
		
		}
	}


		

		
		


		
}

