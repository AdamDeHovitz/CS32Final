bulkAppControllers.controller("feedCtrl", function($scope, $rootScope, $http) {
	$scope.curTitle = "Feed";
	$scope.curEvents = [];
	$.post("/event-feed", {id: $rootScope.account.id}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
	});

	$scope.curEvents.push({
		title: "Watermelon",
		eventId: 1,
		author: "Barack Obama",
		authorId: "abc123",
		img: "http://cdn.skim.gs/image/upload/v1456343706/msi/isolated-slice-of-watermelon_w6khuv.jpg",
		location: "Costco",
		description: "come buy lit watermelons",
		price: 2,
		curMemberNum: 2,
		desiredMembers: 5,
		tags: ["watermelon", "produce", "fresh", "fruit"]
	});
	$scope.curEvents.push({
		title: "Turkey",
		eventId: 2,
		author: "Barack Obama",
		authorId: "abc123",
		img: "http://foodnetwork.sndimg.com/content/dam/images/food/fullset/2010/10/4/0/FNM_110110-Cover-008-no-dial_s4x3.jpg",
		location: "Costco",
		description: "come buy turkeys even doe it ain't thxgiving!!",
		price: 10,
		curMemberNum: 1,
		desiredMembers: 3,
		tags: ["turkey", "meat", "bird", "holiday"]
	});
	

		/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
			})
		});
		$http.post("/feedEntries", postData).success(function(data, status) {
			$scope.events = data.entries;
		});*/
});