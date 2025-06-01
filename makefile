run-client:
	rsync -avz \
		--delete \
		--exclude='.git/' \
		--exclude='run' \
		--exclude='build' \
		home-server:/home/kaynooo/Workspace/minecraft/economy/economy_mod ~/Workspace/minecraft/economy/
	./gradlew runClient
