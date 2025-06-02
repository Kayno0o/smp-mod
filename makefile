sync-from-server:
	rsync -avz \
		--delete \
		--exclude='.git/' \
		--exclude='run' \
		--exclude='build' \
		home-server:/home/kaynooo/Workspace/minecraft/economy/economy_mod/ ~/Workspace/minecraft/economy/economy_mod/

sync-resources:
	rsync -avz \
		--update \
		home-server:/home/kaynooo/Workspace/minecraft/economy/economy_mod/src/main/resources/ ~/Workspace/minecraft/economy/economy_mod/src/main/resources/
	rsync -avz \
		--update \
		~/Workspace/minecraft/economy/economy_mod/src/main/resources/ home-server:/home/kaynooo/Workspace/minecraft/economy/economy_mod/src/main/resources/

run-client: sync-resources sync-from-server
	./gradlew runClient

.PHONY: sync-from-server sync-resources run-client
