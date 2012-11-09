package cs224n.corefsystems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs224n.coref.ClusteredMention;
import cs224n.coref.Document;
import cs224n.coref.Entity;
import cs224n.util.Pair;
import cs224n.coref.*;

public class RuleBased implements CoreferenceSystem {

	private List<RuleSet> passes;
	public void setUpRules(){
		//Set up the passes for this Rule Base Coreference system.
		// Each pass is represented by a ruleset which essentially has some rules to generate candidate coreferences and 
		// some filters to remove some.
		// Ideally the pass setting up should not be "manual" but should be parametrized as commandline parameters
		// But that is not something I wanna get into now.
		passes = new ArrayList<RuleSet>();
		List<Rule> rulesFirstPass = new ArrayList<Rule>();
		rulesFirstPass.add(new ExactMatchRule());
		rulesFirstPass.add(new ApproximateMatchRule());
		List<Filter> filtersFirstPass = new ArrayList<Filter>();
		RuleSet firstPass = new RuleSet(rulesFirstPass,filtersFirstPass);
		passes.add(firstPass);

		List<Rule> rulesSecondPass = new ArrayList<Rule>();
		rulesSecondPass.add(new HeadMatchingRule());
		List<Filter> filtersSecondPass = new ArrayList<Filter>();
		//rulesSecondPass.add(new NaivePronounRule());
		RuleSet secondPass = new RuleSet(rulesSecondPass,filtersSecondPass);
		passes.add(secondPass);
		
	}
	@Override
	public void train(Collection<Pair<Document, List<Entity>>> trainingData) {
		// TODO Auto-generated method stub
		this.setUpRules();
	}

	@Override
	public List<ClusteredMention> runCoreference(Document doc) {
		// TODO Auto-generated method stub
		List<ClusteredMention> mentions = new ArrayList<ClusteredMention>();
		mentions= new AllSingleton().runCoreference(doc);
		Map<String,Entity> clusters = new HashMap<String, Entity>();
		for(RuleSet r : this.passes){
			mentions = r.apply(mentions,doc);
		}
		return mentions;
	}

}
