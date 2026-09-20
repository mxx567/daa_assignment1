<h1>Logistics Factory Patterns</h1>
<h2>Project Purpose</h2>
  <p>
      This project demonstrates two creational design patterns in Java:
      <strong>Factory Method</strong> and <strong>Abstract Factory</strong>.
  </p>

  <h2>Project Structure</h2>
  <pre>
    src/
    ├── classes/
    │   ├── Button.java
    │   ├── CheckBox.java
    │   ├── GUIFactory.java
    │   ├── Logistics.java
    │   ├── MacOSButton.java
    │   ├── MacOSCheckBox.java
    │   ├── MacOSFactory.java
    │   ├── RoadLogistics.java
    │   ├── SeaLogistics.java
    │   ├── Ship.java
    │   ├── Transport.java
    │   ├── Truck.java
    │   ├── WindowsButton.java
    │   ├── WindowsCheckBox.java
    │   └── WindowsFactory.java
    │
    ├── DeliveryApplication.java
    └── Main.java
        </pre>
  <h2>Factory Method</h2>
  <p>
      The Factory Method pattern is used to create transport objects.
  </p>
  <pre>
    Logistics
    ├── RoadLogistics → Truck
    └── SeaLogistics  → Ship
        </pre>
  <p>
      <code>Logistics</code> defines <code>createTransport()</code> and
      provides the shared <code>planDelivery()</code> workflow.
      <code>RoadLogistics</code> creates a <code>Truck</code>, while
      <code>SeaLogistics</code> creates a <code>Ship</code>.
  </p>
  <h2>Abstract Factory</h2>
  <p>
      The Abstract Factory pattern creates matching UI component families.
  </p>
  <pre>
      GUIFactory
      ├── WindowsFactory → WindowsButton + WindowsCheckBox
      └── MacOSFactory   → MacOSButton + MacOSCheckBox
          </pre>
  <p>
      Each factory creates a button and checkbox belonging to the same
      platform family.
  </p>
  <h2>Prerequisites</h2>
  <ul>
      <li>Java JDK 17 or later</li>
      <li>IntelliJ IDEA or another Java IDE</li>
      <li>No external libraries are required</li>
  </ul>
  <h2>Build and Run</h2>
  <h3>Using IntelliJ IDEA</h3>
  <ol>
      <li>Open the project in IntelliJ IDEA.</li>
      <li>Make sure a Java JDK is configured.</li>
      <li>Open <code>Main.java</code>.</li>
      <li>Run <code>Main.main()</code>.</li>
  </ol>
  <h3>Using Command Line</h3>
  <p>Compile the project:</p>

  <pre>
      javac -d out src/classes/*.java src/DeliveryApplication.java src/Main.java
          </pre>

  <p>Run the application:</p>

  <pre>
      java -cp out Main
          </pre>

  <h2>Supported Input</h2>

  <h3>Delivery Mode</h3>

  <ul>
      <li><code>ROAD</code> — creates <code>RoadLogistics</code> and a <code>Truck</code>.</li>
      <li><code>SEA</code> — creates <code>SeaLogistics</code> and a <code>Ship</code>.</li>
  </ul>

  <h3>UI Platform</h3>

  <ul>
      <li><code>WINDOWS</code> — creates Windows UI components.</li>
      <li><code>MACOS</code> — creates macOS UI components.</li>
  </ul>

  <p>
      Input is case-insensitive. For example, <code>road</code> and
      <code>ROAD</code> are both accepted.
  </p>

  <h2>Supported Combinations</h2>

  <table border="1" cellpadding="8" cellspacing="0">
      <thead>
          <tr>
              <th>Delivery Mode</th>
              <th>UI Platform</th>
              <th>Transport</th>
              <th>UI Components</th>
          </tr>
      </thead>
      <tbody>
          <tr>
              <td>ROAD</td>
              <td>WINDOWS</td>
              <td>Truck</td>
              <td>Windows Button + Windows CheckBox</td>
          </tr>
          <tr>
              <td>ROAD</td>
              <td>MACOS</td>
              <td>Truck</td>
              <td>MacOS Button + MacOS CheckBox</td>
          </tr>
          <tr>
              <td>SEA</td>
              <td>WINDOWS</td>
              <td>Ship</td>
              <td>Windows Button + Windows CheckBox</td>
          </tr>
          <tr>
              <td>SEA</td>
              <td>MACOS</td>
              <td>Ship</td>
              <td>MacOS Button + MacOS CheckBox</td>
          </tr>
      </tbody>
  </table>
  
