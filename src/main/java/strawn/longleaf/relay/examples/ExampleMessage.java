/**
 * 
 * Copyright 2016 David Strawn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package strawn.longleaf.relay.examples;

/**
 * This is a POJO, used to demonstrate sending an object as JSON in the examples
 * 
 * @author David Strawn
 */
public class ExampleMessage {
    
    public String value1;
    public String value2;
    
    public ExampleMessage(String value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
    
}
