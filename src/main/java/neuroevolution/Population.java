package neuroevolution;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class Population
{
	private List<Species> speciesList;
	
	public Population(int size, int inputNodeCount, int outputNodeCount, GeneFactory geneFactory, Random random)
	{
		speciesList = new ArrayList<>();
		
		Stream.generate(() -> new Genome().addInputNodes(inputNodeCount).addOutputNodes(outputNodeCount))
			.map(genome -> genome.mutate(geneFactory, random))
			.limit(size)
			.forEach(this::addGenome);
	}
	
	public void print(PrintStream out)
	{
		speciesList.forEach(species -> species.print(out));
	}
	
	private void addGenome(Genome genome)
	{
		findSpecies(genome)
			.orElseGet(this::addSpecies)
			.addGenome(genome);
	}
	
	private Species addSpecies()
	{
		Species species = new Species();
		speciesList.add(species);
		return species;
	}
	
	private Optional<Species> findSpecies(Genome genome)
	{
		return speciesList.stream()
			.filter(species -> species.isCompatibleWith(genome))
			.findFirst();
	}
}
