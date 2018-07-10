import hello
[
	qos_file "abc.txt"
	qos_library ""
]
node PubNode running pub
[
	main_class "pubnode.PubProgram"
]
node SubNode running sub
[
	main_class "subnode.SubProgram"
]