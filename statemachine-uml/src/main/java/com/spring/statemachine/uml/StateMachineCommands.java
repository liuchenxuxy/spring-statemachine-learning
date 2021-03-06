/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spring.statemachine.uml;

import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StateMachineCommands extends AbstractStateMachineCommands<String, String> {

	@CliCommand(value = "sm event", help = "Sends an event to a state machine")
	public String event(@CliOption(key = { "", "event" }, mandatory = true, help = "The event") final String event) {
		getStateMachine().sendEvent(event);
		return "Event " + event + " send";
	}

}

class AbstractStateMachineCommands<S, E> implements CommandMarker {

	@Autowired
	private StateMachine<S, E> stateMachine;

	protected StateMachine<S, E> getStateMachine() {
		return stateMachine;
	}

	@CliCommand(value = "sm state", help = "Prints current state")
	public String state() {
		State<S, E> state = stateMachine.getState();
		if (state != null) {
			return StringUtils.collectionToCommaDelimitedString(state.getIds());
		} else {
			return "No state";
		}
	}

	@CliCommand(value = "sm start", help = "Start a state machine")
	public String start() {
		stateMachine.start();
		return "State machine started";
	}

	@CliCommand(value = "sm stop", help = "Stop a state machine")
	public String stop() {
		stateMachine.stop();
		return "State machine stopped";
	}

	@CliCommand(value = "sm variables", help = "Prints extended state variables")
	public String variables() {
		StringBuilder buf = new StringBuilder();
		Set<Entry<Object, Object>> entrySet = stateMachine.getExtendedState().getVariables().entrySet();
		Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
		if (entrySet.size() > 0) {
			while (iterator.hasNext()) {
				Entry<Object, Object> e = iterator.next();
				buf.append(e.getKey() + "=" + e.getValue());
				if (iterator.hasNext()) {
					buf.append("\n");
				}
			}
		} else {
			buf.append("No variables");
		}
		return buf.toString();
	}

}