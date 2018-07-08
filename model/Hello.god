import hello
node PubNode running pub
[
	platform armv8_linux
	main_class "pubnode.PubProgram"
]
node SubNode running sub
[
	platform armv8_linux
	main_class "subnode.SubProgram"
]