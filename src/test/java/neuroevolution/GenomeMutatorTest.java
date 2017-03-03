package neuroevolution;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import static java.util.stream.Collectors.toList;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static neuroevolution.NodeGene.newInput;
import static neuroevolution.NodeGene.newOutput;

public class GenomeMutatorTest
{
	private GeneFactory geneFactory;
	
	private Random random;
	
	private GenomeMutator mutator;
	
	private NodeGene input1;
	
	private NodeGene input2;
	
	private NodeGene input3;
	
	private NodeGene output;
	
	@Before
	public void setUp()
	{
		geneFactory = new GeneFactory();
		random = mock(Random.class);
		mutator = new GenomeMutator(geneFactory, random);
		
		input1 = newInput();
		input2 = newInput();
		input3 = newInput();
		output = newOutput();
	}
	
	@Test
	public void canMutateConnectionWeights()
	{
		when(random.nextDouble()).thenReturn(0.4, 0.5, 0.6);
		Genome genome = new Genome(input1, input2, input3, output)
			.addGene(new ConnectionGene(input1, output, 0.1, 1))
			.addGene(new ConnectionGene(input2, output, 0.2, 2))
			.addGene(new ConnectionGene(input3, output, 0.3, 3));
		
		Genome result = mutator.mutateConnectionWeights(genome);
		
		assertThat(result.getConnectionGenes().collect(toList()), contains(
			new ConnectionGene(input1, output, 0.08, 1),
			new ConnectionGene(input2, output, 0.2, 2),
			new ConnectionGene(input3, output, 0.32, 3)
		));
	}
	
	@Test
	public void canMutateConnections()
	{
		when(random.nextInt(anyInt())).thenReturn(2, 3);
		when(random.nextDouble()).thenReturn(0.3);
		Genome genome = new Genome(input1, input2, input3, output)
			.addGene(geneFactory.newConnectionGene(input1, output, 0.1))
			.addGene(geneFactory.newConnectionGene(input2, output, 0.2));
		
		Genome result = mutator.mutateConnections(genome);
		
		assertThat(result.getConnectionGenes().collect(toList()), contains(
			new ConnectionGene(input1, output, 0.1, 1),
			new ConnectionGene(input2, output, 0.2, 2),
			new ConnectionGene(input3, output, 0.3, 3)
		));
	}
	
	@Test
	public void canMutateNodes()
	{
		Genome genome = new Genome();
		
		Genome result = mutator.mutateNodes(genome);
		
		// TODO: assert
	}
}